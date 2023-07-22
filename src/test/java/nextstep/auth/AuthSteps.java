package nextstep.auth;

import io.restassured.RestAssured;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class AuthSteps {
    private AuthSteps() {
    }

    public static String 회원_토큰_생성(String email, String password) {
        final Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when().post("/login/token")
                .then().log().all()
                .extract().jsonPath()
                .getString("accessToken");
    }
}
