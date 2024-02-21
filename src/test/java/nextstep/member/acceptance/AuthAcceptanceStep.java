package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.request.TokenRequest;
import nextstep.member.application.response.TokenResponse;
import org.springframework.http.MediaType;

public class AuthAcceptanceStep {

    public static ExtractableResponse<Response> 로그인_시도(TokenRequest tokenRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static String 로그인_성공(String email, String password) {
        return 로그인_시도(TokenRequest.of(email, password)).as(TokenResponse.class).getAccessToken();
    }

}
