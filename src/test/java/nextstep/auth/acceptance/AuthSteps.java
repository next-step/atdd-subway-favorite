package nextstep.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.application.dto.TokenRequest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthSteps {

    public static ExtractableResponse<Response> 로그인_요청(String username, String password) {
        TokenRequest body = new TokenRequest(username, password);

        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 소셜_로그인_요청(int statusCode, String socialType, String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);

        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/" + socialType)
                .then().log().all()
                .statusCode(statusCode)
                .extract();
    }

}
