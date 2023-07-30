package nextstep.auth;

import io.restassured.RestAssured;
import nextstep.auth.token.TokenRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthSteps {
    public static String 로그인_요청_후_토큰발급(String email, String password) {
        return RestAssured.given().log().all()
                .body(new TokenRequest(email, password))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getString("accessToken");
    }
}
