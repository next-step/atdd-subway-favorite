package nextstep.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthSteps {

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/login/token")
            .then().log().all()
            .statusCode(HttpStatus.OK.value()).extract();
    }

    public static String 로그인_후_토큰_반환(String email, String password) {
        ExtractableResponse<Response> response = 로그인_요청(email, password);

        String 로그인_토큰 = responseToAccessToken(response);
        assertThat(로그인_토큰).isNotBlank();

        return 로그인_토큰;
    }

    public static String responseToAccessToken(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("accessToken");
    }

    public static ExtractableResponse<Response> 깃허브_로그인_요청(String code, String accessToken) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", "Bearer " + accessToken)
            .body(params)
            .when().post("/login/github")
            .then().log().all()
            .statusCode(HttpStatus.OK.value()).extract();

        return response;
    }
}
