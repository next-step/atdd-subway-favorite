package nextstep.utils.api;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class AuthApi {
    public static String 로그인으로_토큰_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        ExtractableResponse<Response> loginResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/auth/login")
                .then().log().all()
                .extract();

        return loginResponse.jsonPath().getString("accessToken");
    }

    public static String 깃헙_로그인으로_토큰_요청(String githubUserCode) {
        Map<String, String> params = new HashMap<>();
        params.put("code", githubUserCode);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/auth/login/github")
                .then().log().all()
                .extract();

        return response.jsonPath().getString("accessToken");
    }
}
