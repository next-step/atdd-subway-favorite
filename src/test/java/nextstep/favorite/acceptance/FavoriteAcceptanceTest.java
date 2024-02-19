package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    public static final String OTHER_EMAIL = "test@naver.com";
    public static final String OHTER_PASSWORD = "test_password";
    public static final Integer OTHER_AGE = 20;

    public static String accessToken;
    private Long 강남역;
    private Long 역삼역;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        역삼역 = 지하철역_생성_요청("역삼역").jsonPath().getLong("id");
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        accessToken = 토큰(EMAIL, PASSWORD);
    }


    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {

        int statusCode = 즐겨찾기_생성(accessToken, 강남역, 역삼역).statusCode();
        assertThat(statusCode).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void getFavorites() {
        즐겨찾기_생성(accessToken, 강남역, 역삼역);

        List<Long> ids = 모든_즐겨찾기_조회(accessToken).jsonPath().getList("id", Long.class);
        assertThat(ids).hasSize(1);
    }


    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite() {
        즐겨찾기_생성(accessToken, 강남역, 역삼역);
        List<Long> ids = 모든_즐겨찾기_조회(accessToken).jsonPath().getList("id", Long.class);

        RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/favorites/{id}", ids.get(0))
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("다른 사용자의 즐겨찾기를 삭제한다.")
    @Test
    void deleteFavoriteOfOtherUser() {
        즐겨찾기_생성(accessToken, 강남역, 역삼역);
        List<Long> ids = 모든_즐겨찾기_조회(accessToken).jsonPath().getList("id", Long.class);


        회원_생성_요청(OTHER_EMAIL, OHTER_PASSWORD, OTHER_AGE);
        String otherAccessToken = 토큰(OTHER_EMAIL, OHTER_PASSWORD);

        RestAssured.given().log().all()
                .auth().oauth2(otherAccessToken)
                .when().delete("/favorites/{id}", ids.get(0))
                .then().log().all()
                .statusCode(400);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성(String accessToken, Long source, Long target) {
        return RestAssured.given().log().all()
                .contentType("application/json")
                .body(new HashMap<String, String>(){{
                    put("source", String.valueOf(source));
                    put("target", String.valueOf(target));
                }})
                .auth().oauth2(accessToken)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    private String 토큰(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
        String accessToken = response.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotBlank();
        return accessToken;
    }

    private static ExtractableResponse<Response> 모든_즐겨찾기_조회(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all().extract();
    }
}
