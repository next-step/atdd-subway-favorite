package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.member.application.dto.GithubTokenRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthSteps {

    public static ExtractableResponse<Response> 로그인_토큰발급_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static String 로그인_토큰발급_요청_후_토큰_반환(String email, String password) {
        return 로그인_토큰발급_요청(email, password).jsonPath().getString("accessToken");
    }

    public static ExtractableResponse<Response> 깃헙_로그인_요청(GithubTokenRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/login/github")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
    }
}
