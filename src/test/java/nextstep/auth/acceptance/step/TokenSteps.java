package nextstep.auth.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.token.TokenRequest;
import org.springframework.http.MediaType;

import java.util.Map;

public class TokenSteps {
    private TokenSteps() {
    }

    public static ExtractableResponse<Response> 일반_로그인_요청(String email, String password) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(email, password))
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 깃허브_로그인_요청(String code) {
        Map<String, String> params = Map.of("code", code);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/github")
                .then().log().all()
                .extract();
    }

    public static String 토큰_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("accessToken");
    }
}
