package nextstep.subway.acceptance.auth;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import static nextstep.subway.acceptance.member.MemberSteps.PASSWORD_FIELD;
import static nextstep.subway.acceptance.member.MemberSteps.USERNAME_FIELD;

public class AuthSteps {
    public static final String ADMIN_EMAIL = "admin@email.com";
    public static final String ADMIN_PASSWORD = "password";

    public static ExtractableResponse<Response> 베이직_인증으로_내_회원_정보_조회_요청(String username, String password) {
        return RestAssured.given().log().all()
                .auth().preemptive().basic(username, password)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 폼_로그인_후_내_회원_정보_조회_요청(String email, String password) {
        return RestAssured.given().log().all()
                .auth().form(email, password, new FormAuthConfig("/login/form", USERNAME_FIELD, PASSWORD_FIELD))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 베어러_인증으로_내_회원_정보_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when()
                .get("/members/me")
                .then().log().all()
                .extract();
    }
}
