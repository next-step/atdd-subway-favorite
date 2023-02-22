package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AcceptanceTest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.member.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 즐겨찾기 관리 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private Long 강남역;
    private Long 판교역;
    private Long 정자역;

    /**
     * Given 회원 가입을 하고
     * Given 역을 추가하고
     */
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        판교역 = 지하철역_생성_요청("판교역").jsonPath().getLong("id");
        정자역 = 지하철역_생성_요청("정자역").jsonPath().getLong("id");
    }

    /**
     * Given 로그인하고
     * When 지하철 노선을 즐겨찾기 등록 요청하면
     * Then 즐겨찾기 등록을 성공한다.
     */
    @DisplayName("로그인하고 즐겨찾기 등록 요청하면 성공한다.")
    @Test
    void saveFavorite() {
        // given
        String accessToken = 로그인_요청(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> saveResponse = 즐겨찾기_생성_요청(accessToken, 강남역, 판교역);

        // then
        ExtractableResponse<Response> findResponse = 즐겨찾기_조회_요청(accessToken, saveResponse);
        assertThat(saveResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(findResponse.jsonPath().getLong("source.id")).isEqualTo(판교역);
        assertThat(findResponse.jsonPath().getLong("target.id")).isEqualTo(강남역);
    }

    /**
     * Given 로그인하고
     * When 존재하지 않는 지하철 노선을 즐겨찾기 등록 요청하면
     * Then 즐겨찾기 등록을 실패한다.
     */
    @DisplayName("로그인하고 존재하지 않는 노선을 즐겨찾기 등록 요청하면 실패한다.")
    @Test
    void saveNotFoundLineFavorite() {
        // given
        String accessToken = 로그인_요청(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> saveResponse = 즐겨찾기_생성_요청(accessToken, Long.MAX_VALUE, Long.MAX_VALUE);

        // then
        assertThat(saveResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 비로그인으로
     * When 지하철 노선을 즐겨찾기 등록 요청하면
     * Then 권한이 없어 즐겨찾기 등록을 실패한다.
     */
    @DisplayName("비로그인으로 즐겨찾기 등록 요청하면 권한이 없어서 실패한다.")
    @Test
    void unauthorizedSaveFavorite() {
        // given
        String accessToken = "faketoken";

        // when
        ExtractableResponse<Response> saveResponse = 즐겨찾기_생성_요청(accessToken, 강남역, 판교역);

        // then
        assertThat(saveResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 로그인하고
     * Given 지하철 노선을 즐겨찾기 등록 하고
     * When 즐겨찾기 목록 조회 요청하면
     * Then 등록한 즐겨찾기 목록이 조회된다.
     */
    @DisplayName("즐겨찾기 목록 조회 요청을 성공한다.")
    @Test
    void getFavorites() {
        // given
        String accessToken = 로그인_요청(EMAIL, PASSWORD);
        ExtractableResponse<Response> saveResponse = 즐겨찾기_생성_요청(accessToken, 강남역, 판교역);

        // when
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(accessToken);

        // then
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(extractStationIds(findResponse)).containsExactlyInAnyOrder(강남역, 판교역);
    }

    /**
     * Given 비로그인으로
     * When 즐겨찾기 목록 조회 요청하면
     * Then 권한이 없어 즐겨찾기 목록 조회를 실패한다.
     */
    @DisplayName("비로그인으로 즐겨찾기 목록 조회 요청하면 권한이 없어서 실패한다.")
    @Test
    void unauthorizedGetFavorites() {
        // given
        String accessToken = "faketoken";

        // when
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(accessToken);

        // then
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 로그인하고
     * Given 지하철 노선을 즐겨찾기 등록 하고
     * When 즐겨찾기 삭제 요청하면
     * Then 즐겨찾기 목록에서 조회할 수 없다.
     */
    @DisplayName("즐겨찾기 삭제 요청을 성공한다.")
    @Test
    void deleteFavorite() {
        // given
        String accessToken = 로그인_요청(EMAIL, PASSWORD);
        ExtractableResponse<Response> saveResponse = 즐겨찾기_생성_요청(accessToken, 강남역, 판교역);
        Long favoriteId = saveResponse.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, favoriteId);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 로그인하고
     * Given 지하철 노선을 즐겨찾기 등록 하고
     * When 존재하지 않는 즐겨찾기 항목을 삭제 요청하면
     * Then 즐겨찾기 삭제를 실패한다.
     */
    @DisplayName("존재하지 않는 즐겨찾기를 삭제 요청하면 실패한다.")
    @Test
    void deleteNotFoundFavorite() {
        // given
        String accessToken = 로그인_요청(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, Long.MAX_VALUE);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 비로그인으로
     * When 즐겨찾기 삭제 요청하면
     * Then 권한이 없어 즐겨찾기 삭제를 실패한다.
     */
    @DisplayName("비로그인으로 즐겨찾기를 삭제 요청하면 권한이 없어서 실패한다.")
    @Test
    void unauthorizedDeleteFavorite() {
        // given
        String accessToken = "faketoken";

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, Long.MAX_VALUE);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private String 로그인_요청(String email, String password) {
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(email, password);
        String accessToken = response.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotBlank();
        return accessToken;
    }

    private static List<Long> extractStationIds(ExtractableResponse<Response> findResponse) {
        List<FavoriteResponse> favoriteResponses = findResponse.jsonPath().getList("", FavoriteResponse.class);
        return favoriteResponses.stream()
                .map(favorite -> List.of(favorite.getSource(), favorite.getTarget()))
                .flatMap(Collection::stream)
                .map(StationResponse::getId)
                .collect(Collectors.toList());
    }
}
