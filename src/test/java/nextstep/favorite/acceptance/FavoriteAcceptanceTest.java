package nextstep.favorite.acceptance;

import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.member.domain.Member;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private Member member;
    private String accessToken;
    private Long 북한역;
    private Long 남한역;

    private Long 한국선;
    private Long 강남역;
    private long 양재역;
    private long 양재시민의숲역;
    private long 판교역;
    private long 신사역;
    private long 잠원역;
    private long 고속터미널역;
    private long 교대역;
    private long 남부터미널역;
    private long 신분당선;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        String email = "test@test.com";
        String password = "test";
        int age = 30;
        회원_생성_요청(email, password, age);
        accessToken = 인증_요청_하기(email, password).jsonPath().get("accessToken");
        신분당선_3호선_만들기();
    }

    private void 신분당선_3호선_만들기() {
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        양재시민의숲역 = 지하철역_생성_요청("양재시민의숲역").jsonPath().getLong("id");
        판교역 = 지하철역_생성_요청("판교역").jsonPath().getLong("id");
        신사역 = 지하철역_생성_요청("신사역").jsonPath().getLong("id");
        잠원역 = 지하철역_생성_요청("잠원역").jsonPath().getLong("id");
        고속터미널역 = 지하철역_생성_요청("고속터미널역").jsonPath().getLong("id");
        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
        북한역 = 지하철역_생성_요청("북한역").jsonPath().getLong("id");
        남한역 = 지하철역_생성_요청("남한역").jsonPath().getLong("id");
        Long 신분당선 = 지하철_노선_생성_요청("신분당선", "RED", 강남역, 양재역, 10).jsonPath().getLong("id");
        Long 삼호선 = 지하철_노선_생성_요청("3호선", "YELLOW", 신사역, 잠원역, 10).jsonPath().getLong("id");
        Long 한국선 = 지하철_노선_생성_요청("한국선", "RED", 북한역, 남한역, 10).jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(신분당선, 강남역, 양재역, 10);
        지하철_노선에_지하철_구간_생성_요청(신분당선, 양재역, 판교역, 10);
        지하철_노선에_지하철_구간_생성_요청(신분당선, 양재역, 양재시민의숲역, 10);
        지하철_노선에_지하철_구간_생성_요청(삼호선, 강남역, 신사역, 10);
        지하철_노선에_지하철_구간_생성_요청(삼호선, 신사역, 잠원역, 10);
        지하철_노선에_지하철_구간_생성_요청(삼호선, 잠원역, 고속터미널역, 10);
        지하철_노선에_지하철_구간_생성_요청(삼호선, 고속터미널역, 교대역, 10);
        지하철_노선에_지하철_구간_생성_요청(한국선, 북한역, 남한역, 10);
    }

    private static ExtractableResponse<Response> 인증_요청_하기(String email,
        String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/login/token")
            .then().log().all()
            .statusCode(HttpStatus.OK.value()).extract();
        return response;
    }

    /**
     * Given 지하철 노선이 주어진다.
     * Given source, target 이 주어진다.
     * When 즐겨찾기를 추가한다.
     * Then 즐겨찾기가 추가된다.
     */
    @DisplayName("즐겨찾기를 추가한다.")
    @Test
    void addFavorite() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(강남역));
        params.put("target", String.valueOf(교대역));
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .auth().oauth2(accessToken)
            .body(params)
            .contentType("application/json")
            .when()
            .post("/favorites")
            .then().log().all().extract();
        // then
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.header("Location")).isNotBlank();
    }

    /**
     * Given source, target 이 주어진다.
     * When 즐겨찾기를 추가한다.
     * Then 즐겨찾기가 추가 되지 않고 예외가 발생한다.
     */
    @DisplayName("즐겨찾기를 추가하면 예외가 발생한다")
    @Test
    void addFavoriteNotExistsLocation() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("source", "1");
        params.put("target", String.valueOf(북한역));
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .auth().oauth2(accessToken)
            .body(params)
            .contentType("application/json")
            .when()
            .post("/favorites")
            .then().log().all().extract();
        // then
        assertThat(response.statusCode()).isEqualTo(400);
    }

    /**
     * Given 즐겨찾기가 주어진다.
     * When 즐겨찾기를 조회한다.
     * Then 즐겨찾기와 경로가 조회된다
     */
    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void getFavorites() {
        // given
        FavoriteSteps.즐겨찾기_생성_요청(accessToken, 강남역, 교대역);
        FavoriteSteps.즐겨찾기_생성_요청(accessToken, 강남역, 신사역);
        // when
        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
            .auth().oauth2(accessToken)
            .when()
            .get("/favorites")
            .then().log().all().extract();
        // then
        assertThat(response2.statusCode()).isEqualTo(200);
        assertThat(response2.body().jsonPath().getList("id")).isNotEmpty();
    }
}
