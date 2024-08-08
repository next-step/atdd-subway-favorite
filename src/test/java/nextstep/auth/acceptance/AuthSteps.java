package nextstep.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.fake.github.GithubStaticUsers;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class AuthSteps {
    public static ExtractableResponse<Response> 이메일_패스워드_로그인(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 깃헙_로그인(GithubStaticUsers githubStaticUsers) {
        return 깃헙_로그인(githubStaticUsers.getCode());
    }

    public static ExtractableResponse<Response> 깃헙_로그인(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/github")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static String 인증토큰을_추출한다(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("accessToken");
    }
}