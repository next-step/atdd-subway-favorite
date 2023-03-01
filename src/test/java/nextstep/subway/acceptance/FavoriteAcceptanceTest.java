package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_삭제;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_생성;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_조회;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_조회_검증;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관리 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String email = "test@email.com";
    private static final String password = "password";

    private long 신논현역;
    private long 강남역;
    private long 양재역;

    @BeforeEach
    public void setUp() {
        신논현역 = getStationId(지하철역_생성_요청("신논현역"));
        강남역 = getStationId(지하철역_생성_요청("강남역"));
        양재역 = getStationId(지하철역_생성_요청("양재역"));
        MemberSteps.회원_생성_요청(email, password, 20);
    }

    /**
     * Given 회원의 token이 주어졌을 때
     * When 즐겨찾기를 생성하면
     * Then 즐겨찾기를 생성할 수 있다.
     */
    @DisplayName("즐겨찾기 생성")
    @Test
    void createFavorite() {
        // given
        ExtractableResponse<Response> tokenResponse = MemberSteps.베어러_인증_로그인_요청(email, password);
        String accessToken = tokenResponse.jsonPath().getString("accessToken");

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성(String.valueOf(신논현역), String.valueOf(양재역), accessToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 잘못된 토큰이 주어졌을 때
     * When 즐겨찾기를 생성하면
     * Then 권한 예외를 받는다ㄹ
     */
    @DisplayName("즐겨찾기 생성 비로그인")
    @Test
    void createFavoriteNoLogin() {
        // given

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성(String.valueOf(신논현역), String.valueOf(양재역), "noLogin");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 즐겨찾기를 등록하고
     * When 즐겨찾기를 조회하면
     * Then 회원의 즐겨찾기가 모두 조회된다.
     */
    @DisplayName("즐겨찾기 조회")
    @Test
    void getFavorites() {
        // given
        ExtractableResponse<Response> tokenResponse = MemberSteps.베어러_인증_로그인_요청(email, password);
        String accessToken = tokenResponse.jsonPath().getString("accessToken");

        즐겨찾기_생성(String.valueOf(신논현역), String.valueOf(양재역), accessToken);
        즐겨찾기_생성(String.valueOf(강남역), String.valueOf(양재역), accessToken);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회(accessToken);

        // then
        즐겨찾기_조회_검증(response, List.of("신논현역", "강남역"), List.of("양재역", "양재역"));
    }

    /**
     * Given 즐겨찾기를 등록하고
     * When 즐겨찾기를 삭제하면
     * Then 삭제한 즐겨찾기를 제외한 즐겨찾
     */
    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        // given
        ExtractableResponse<Response> tokenResponse = MemberSteps.베어러_인증_로그인_요청(email, password);
        String accessToken = tokenResponse.jsonPath().getString("accessToken");

        long createdFavoriteId =
                getCreatedFavoriteLocation(즐겨찾기_생성(String.valueOf(신논현역), String.valueOf(양재역), accessToken));
        즐겨찾기_생성(String.valueOf(강남역), String.valueOf(양재역), accessToken);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제(createdFavoriteId, accessToken);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        ExtractableResponse<Response> response = 즐겨찾기_조회(accessToken);
        즐겨찾기_조회_검증(response, List.of("강남역"), List.of("양재역"));
    }

    private long getStationId(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }

    private long getCreatedFavoriteLocation(ExtractableResponse<Response> response) {
        String location = response.header(HttpHeaders.LOCATION);
        return Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
    }
}
