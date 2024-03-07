package nextstep.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.application.dto.OAuth2LoginRequest;
import org.springframework.http.MediaType;

public class AuthSteps {

    public static ExtractableResponse<Response> 깃허브_토큰_발급(String code) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OAuth2LoginRequest(code))
                .when().post("/github/login/oauth/access_token")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 깃허브_정보_조회(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/github/user")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 깃허브_로그인_요청(String code) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OAuth2LoginRequest(code))
                .when().post("/login/github")
                .then().log().all()
                .extract();
    }
}
