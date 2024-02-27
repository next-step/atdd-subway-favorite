package nextstep.favorite.acceptance;

import static nextstep.favorite.acceptance.FavoriteSteps.신분당선_3호선_만들기;
import static nextstep.favorite.acceptance.FavoriteSteps.지하철_노선_생성_요청;
import static nextstep.favorite.acceptance.FavoriteSteps.지하철역_생성_요청;
import static nextstep.favorite.acceptance.FavoriteSteps.회원_생성_요청;
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
        북한역 = 지하철역_생성_요청("북한역").jsonPath().getLong("id");
        남한역 = 지하철역_생성_요청("남한역").jsonPath().getLong("id");
        한국선 = 지하철_노선_생성_요청("한국선", "RED", 북한역, 남한역, 10).jsonPath().getLong("id");
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
        params.put("source", "1");
        params.put("target", "2");
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
     * Given 지하철 노선이 주어진다.
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
     *
     */
}
