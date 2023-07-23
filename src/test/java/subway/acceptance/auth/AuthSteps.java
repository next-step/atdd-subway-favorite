package subway.acceptance.auth;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class AuthSteps {

    public static ExtractableResponse<Response> 로그인_API(final Map<String, String> auth) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(auth)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }
}
