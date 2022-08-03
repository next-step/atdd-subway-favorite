package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.DataLoader.ADMIN_EMAIL;
import static nextstep.DataLoader.AGE;
import static nextstep.DataLoader.MEMBER_EMAIL;
import static nextstep.DataLoader.PASSWORD;
import static nextstep.subway.acceptance.AuthSteps.로그인_되어_있음;
import static org.assertj.core.api.Assertions.assertThat;


class AuthAcceptanceTest extends AcceptanceTest {

    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";

    @DisplayName("Basic Auth")
    @Test
    void myInfoWithBasicAuth() {
        var response = 베이직_인증으로_내_회원_정보_조회_요청(ADMIN_EMAIL, PASSWORD);

        회원_정보_조회됨(response, ADMIN_EMAIL, AGE);
    }

    @DisplayName("Session 로그인 후 내 정보 조회")
    @Test
    void myInfoWithSession() {
        var response = 폼_로그인_후_내_회원_정보_조회_요청(MEMBER_EMAIL, PASSWORD);

        회원_정보_조회됨(response, MEMBER_EMAIL, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        String accessToken = 로그인_되어_있음(ADMIN_EMAIL, PASSWORD);

        var response = 베어러_인증으로_내_회원_정보_조회_요청(accessToken);

        회원_정보_조회됨(response, ADMIN_EMAIL, AGE);
    }

    private ExtractableResponse<Response> 베이직_인증으로_내_회원_정보_조회_요청(String email, String password) {
        return RestAssured.given().log().all()
                .auth().preemptive().basic(email, password)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private ExtractableResponse<Response> 폼_로그인_후_내_회원_정보_조회_요청(String email, String password) {
        return RestAssured.given().log().all()
                .auth().form(email, password,
                        new FormAuthConfig("/login/form", USERNAME_FIELD, PASSWORD_FIELD))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private ExtractableResponse<Response> 베어러_인증으로_내_회원_정보_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        assertThat(response.jsonPath().getString("id")).isNotNull();
        assertThat(response.jsonPath().getString("email")).isEqualTo(email);
        assertThat(response.jsonPath().getInt("age")).isEqualTo(age);
    }
}
