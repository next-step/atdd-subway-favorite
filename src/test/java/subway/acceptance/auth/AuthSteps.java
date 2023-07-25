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

    public static ExtractableResponse<Response> GITHUB_CODE_API(final Map<String, String> auth) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(auth)
                .when().post("/login/github")
                .then().log().all()
                .extract();
    }


    public static ExtractableResponse<Response> 임의의_로그인_API() {
        var response = RestAssured.given().log().all()
                .auth().oauth2("none.none.none")
                .when().get("/members/me")
                .then().log().all()
                .extract();
        return response;
    }
}
