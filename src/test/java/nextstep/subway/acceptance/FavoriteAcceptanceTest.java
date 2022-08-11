package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관리 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 강남역;
    private Long 양재역;

    private String 사용자_token;

    /**
     * Given 지하철역과 노선 생성을 요청 하고
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_생성_요청(관리자_token, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자_token, "양재역").jsonPath().getLong("id");

        사용자_token = 로그인_되어_있음("user@email.com", "user");
    }

    /**
     * When 즐겨찾기 생성 요청을 하면
     * Then 성공한다.
     */
    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(사용자_token, 강남역, 양재역);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * When 즐겨찾기를 만들고, 조회 요청을 하면
     * Then 보여준다.
     */
    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void getFavorites() {
        즐겨찾기_생성_요청(사용자_token, 강남역, 양재역);

        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(사용자_token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getLong("source.id")).isEqualTo(강남역);
        assertThat(response.jsonPath().getLong("target.id")).isEqualTo(양재역);
    }

    /**
     * When 즐겨찾기를 만들고, 삭제 요청을 하면
     * Then 삭제된다.
     */
    @DisplayName("즐겨찾기를 제거한다.")
    @Test
    void deleteFavorite() {
        즐겨찾기_생성_요청(사용자_token, 강남역, 양재역);
        long id = 즐겨찾기_조회_요청(사용자_token).jsonPath().getLong("id");

        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(사용자_token, id);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * When 로그인 정보가 없는 유저가 접근하려고 하면
     * Then 401을 리턴한다.
     */
    @DisplayName("로그인 안한 유저는 즐겨찾기를 생성하지 못한다.")
    @Test
    void notLoginUser() {
        String dummyToken = 로그인_되어_있음("dummy@email.com", "dummy");
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(dummyToken, 강남역, 양재역);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
