package nextstep.subway.acceptance.auth;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import static nextstep.subway.utils.GivenUtils.PASSWORD;
import static nextstep.subway.utils.GivenUtils.USERNAME;

@Component
@RequiredArgsConstructor
public class AuthSteps {

    private static final String MEMBERS_ME_PATH = "/members/me";
    private static final String LOGIN_FORM = "/login/form";

    public static ExtractableResponse<Response> 베이직_인증으로_회원_정보_조회(String username, String password) {
        return RestAssured.given().log().all()
                .auth().preemptive().basic(username, password)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(MEMBERS_ME_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 폼_로그인_후_회원_정보_조회(String email, String password) {
        return RestAssured.given().log().all()
                .auth().form(email, password, new FormAuthConfig(LOGIN_FORM, USERNAME, PASSWORD))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(MEMBERS_ME_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 베어러_인증으로_회원_정보_조회(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get(MEMBERS_ME_PATH)
                .then().log().all()
                .extract();
    }

}
