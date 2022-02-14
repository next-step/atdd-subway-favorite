package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class AuthSteps {
    private static final String AUTHENTICATE_EMAIL_FIELD = "email";
    private static final String AUTHENTICATE_PASSWORD_FIELD = "password";
    private static final String ACCESS_TOKEN_FIELD = "accessToken";

    public static String 토큰_인증(String email, String password) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put(AUTHENTICATE_EMAIL_FIELD, email);
        requestBody.put(AUTHENTICATE_PASSWORD_FIELD, password);

        ExtractableResponse<Response> postResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)

                .when()
                .post("/login/token")

                .then().log().all().extract();

        return postResponse.jsonPath().getString(ACCESS_TOKEN_FIELD);
    }
}
