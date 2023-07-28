package nextstep.favorties.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.utils.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.member.acceptance.TokenSteps.로그인_요청;
import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 기능")
public class FavoritesAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**
     * 교대역    --- *2호선* ---   강남역
     *                         |
     *                       *신분당선*
     *                         |
     * 남부터미널역               양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10);
    }

    /**
     * Given 사용자가 회원가입과 로그인을 하고
     * When 즐겨찾기 생성을 요청하면
     * Then 즐겨찾기가 생성되고
     * When 즐겨찾기를 조회하면
     * Then 생성된 즐겨찾기가 조회되고
     * When 즐겨찾기를 삭제하면
     * Then 즐겨찾기가 삭제되고
     * When 즐겨찾기를 조회하면
     * Then 즐겨찾기가 조회되지 않는다.
     */
    @DisplayName("즐겨찾기를 생성/조회/삭제한다.")
    @Test
    void saveAndDeleteFavorites() {

        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");

        // when
        ExtractableResponse<Response> responseSave = 즐겨찾기_생성(accessToken, 교대역, 양재역);

        // then
        assertThat(responseSave.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> responseFavorites = 즐겨찾기_목록_조회(accessToken);

        // then
        assertThat(responseFavorites.statusCode()).isEqualTo(HttpStatus.OK.value());

        Long favoriteId = responseFavorites.jsonPath().getLong("$.[0].id");

        assertThat(favoriteId).isNotNull();
        assertThat(responseFavorites.jsonPath().getLong("$.[0].source.id")).isEqualTo(교대역);
        assertThat(responseFavorites.jsonPath().getLong("$.[0].target.id")).isEqualTo(양재역);

        // when
        ExtractableResponse<Response> responseDelete = 즐겨찾기_삭제(accessToken, favoriteId);

        // then
        assertThat(responseDelete.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        ExtractableResponse<Response> responseFavoritesForVerify = 즐겨찾기_목록_조회(accessToken);
        assertThat(responseFavoritesForVerify.jsonPath().getList("$")).hasSize(0);
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제(String accessToken, Long favoriteId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/favorites/{favoriteId}", favoriteId)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_생성(String accessToken, Long 교대역, Long 양재역) {
        Map<String, String> params = new HashMap<>();
        params.put("source", 교대역.toString());
        params.put("target", 양재역.toString());

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    /**
     * Given 사용자가 로그인을 하지 않고
     * When 즐겨찾기 저장을 하면
     * Then 오류가 나고
     * When 즐겨찾기를 조회하면
     * Then 오류가 나고
     * When 즐겨찾기를 삭제하면
     * Then 오류가 난다.
     */
    @DisplayName("로그인하지 않은 사용자가 즐겨찾기를 생성/조회/삭제한다.")
    @Test
    void saveAndDeleteFavorites_noLogin() {
        // given
        String accessToken = "wrong-access-token";

        // when
        ExtractableResponse<Response> responseSave = 즐겨찾기_생성(accessToken, 교대역, 양재역);

        // then
        assertThat(responseSave.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

        // when
        ExtractableResponse<Response> responseFavorites = 즐겨찾기_목록_조회(accessToken);

        // then
        assertThat(responseFavorites.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

        // when
        ExtractableResponse<Response> responseDelete = 즐겨찾기_삭제(accessToken, 1L);

        // then
        assertThat(responseDelete.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 로그인을 한 사용자가
     * When 찾을 수 없는 경로를 즐겨찾기 저장을 하면
     * Then 오류가 난다.
     */
    @DisplayName("로그인하지 않은 사용자가 즐겨찾기를 생성/조회/삭제한다.")
    @Test
    void saveFavorites_wrongPath() {

        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");

        // when
        ExtractableResponse<Response> responseSave = 즐겨찾기_생성(accessToken, 교대역, 남부터미널역);

        // then
        assertThat(responseSave.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
