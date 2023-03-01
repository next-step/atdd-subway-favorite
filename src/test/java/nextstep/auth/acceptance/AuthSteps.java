package nextstep.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

public class AuthSteps {
    public static ExtractableResponse<Response> 깃헙_로그인_요청_성공(Map<String, String> params) {
        return 깃헙_로그인_요청(params)
                .statusCode(HttpStatus.OK.value()).extract();
    }

    public static ExtractableResponse<Response> 깃헙_로그인_요청_실패(Map<String, String> params) {
        return 깃헙_로그인_요청(params)
                .statusCode(HttpStatus.UNAUTHORIZED.value()).extract();
    }

    private static ValidatableResponse 깃헙_로그인_요청(Map<String, String> params) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/github")
                .then().log().all();
    }
}
