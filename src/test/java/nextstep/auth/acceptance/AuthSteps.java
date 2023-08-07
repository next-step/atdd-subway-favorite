package nextstep.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.common.CommonSteps.*;

public class AuthSteps {

    private AuthSteps() {}

    public static ExtractableResponse<Response> 토큰_로그인_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .extract();
        return response;
    }

    public static String 토큰_로그인_요청_성공(String email, String password) {
        ExtractableResponse<Response> response = 토큰_로그인_요청(email, password);
        checkHttpResponseCode(response, HttpStatus.OK);
        return response.jsonPath().getString("accessToken");
    }

    public static ExtractableResponse<Response> 깃허브_로그인_요청(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/github")
                .then().log().all()
                .extract();
        return response;
    }

    public static String 깃허브_로그인_요청_성공(String code) {
        ExtractableResponse<Response> response = 깃허브_로그인_요청(code);
        checkHttpResponseCode(response, HttpStatus.OK);
        return response.jsonPath().getString("accessToken");
    }
}
