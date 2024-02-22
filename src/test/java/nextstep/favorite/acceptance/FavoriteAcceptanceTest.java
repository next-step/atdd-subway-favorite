package nextstep.favorite.acceptance;

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

import static nextstep.favorite.acceptance.FavoriteSteps.신분당선_3호선_만들기;
import static nextstep.favorite.acceptance.FavoriteSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private Member member;
    private String accessToken;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        String email = "test@test.com";
        String password = "test";
        int age = 30;
        회원_생성_요청(email, password, age);
        accessToken = 인증_요청_하기(email, password).jsonPath().get("accessToken");
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
        신분당선_3호선_만들기();
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
}
