package nextstep.api.auth.acceptance;

import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class AuthSteps {
    public static ExtractableResponse<Response> 로그인_요청(final String email, final String password) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(Map.of(
                        "email", email,
                        "password", password
                ))
                .when().post("/login/token")
                .then().extract();
    }
}
