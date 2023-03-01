package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.DataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    private String token;
    private Long 강남역;
    private Long 양재역;

    @Autowired
    private DataLoader dataLoader;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");

        dataLoader.loadData();

        token = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
    }

    /**
     * When 즐겨찾기를 생성하고
     * When 즐겨찾기 목록을 조회하면
     * Then 유저의 즐겨찾기 목록에 해당 즐겨찾기가 추가된다
     */
    @Test
    void 즐겨찾기_생성_및_조회() {
        // when
        createFavorite(token, 강남역, 양재역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(token);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("source.id", Long.class)).containsExactly(강남역);
        assertThat(response.jsonPath().getList("target.id", Long.class)).containsExactly(양재역);
    }

    /**
     * Given 존재하지 않는 역이 존재할 떄
     * When 해당 역을 포함하여 즐겨찾기를 생성하면
     * Then 400에러를 반환한다
     */
    @Test
    void 존재하지_않는_역이_포함된_즐겨찾기_생성은_실패() {
        // given
        Long 없는역 = 999L;

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(token, 강남역, 없는역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 잘못된 토큰이 존재할 때
     * When 해당 토큰을 가지고 즐겨찾기를 생성하면
     * Then 401에러를 반환한다
     */
    @Test
    void 잘못된_토큰으로_즐겨찾기_생성은_실패() {
        // given
        String invalidToken = "invalid_token";

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(invalidToken, 강남역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 잘못된 토큰이 존재할 때
     * When 해당 토큰을 가지고 즐겨찾기를 조회하면
     * Then 401에러를 반환한다
     */
    @Test
    void 잘못된_토큰으로_즐겨찾기_조회는_실패() {
        // given
        String invalidToken = "invalid_token";

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(invalidToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 즐겨찾기를 생성하고
     * When 해당 즐겨찾기 삭제를 요청하면
     * Then 유저의 즐겨찾기 목록에 해당 즐겨찾기가 삭제된다
     */
    @Test
    void 즐겨찾기_삭제() {
        // given
        createFavorite(token, 강남역, 양재역);
        Long favoriteId = 즐겨찾기_조회_요청(token).jsonPath().getLong("[0].id");

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(token, favoriteId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 즐겨찾기를 생성하고
     * Given 잘못된 토큰이 존재할 때
     * When 해당 토큰을 가지고 즐겨찾기를 삭제하면
     * Then 401에러를 반환한다
     */
    @Test
    void 잘못된_토큰으로_즐겨찾기_삭제는_실패() {
        // given
        createFavorite(token, 강남역, 양재역);
        Long favoriteId = 즐겨찾기_조회_요청(token).jsonPath().getLong("[0].id");

        // given
        String invalidToken = "invalid_token";

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(invalidToken, favoriteId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void createFavorite(String token, Long source, Long target) {
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(token, source, target);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
