package nextstep.acceptance.test;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.common.dataloader.DataLoader.ADMIN_EMAIL;
import static nextstep.common.dataloader.DataLoader.AGE;
import static nextstep.common.dataloader.DataLoader.MEMBER_EMAIL;
import static nextstep.common.dataloader.DataLoader.PASSWORD;
import static nextstep.acceptance.step.AuthSteps.로그인_되어_있음;
import static org.assertj.core.api.Assertions.assertThat;


class AuthAcceptanceTest extends AcceptanceTest {

    private static final String FORM_USERNAME_FIELD = "username";
    private static final String FORM_PASSWORD_FIELD = "password";
    private static final String INVALID_EMAIL = "asdf@asdf.com";
    private static final String INVALID_PASSWORD = "asdfasdf";

    @DisplayName("Basic Auth로 내 정보 조회")
    @Test
    void myInfoWithBasicAuth() {
        // when
        var response = 베이직_인증으로_내_회원_정보_조회_요청(ADMIN_EMAIL, PASSWORD);

        // then
        회원_정보_조회됨(response, ADMIN_EMAIL, AGE);
    }

    @DisplayName("Basic Auth 실패")
    @Test
    void myInfoWithBasicAuth_Fail() {
        // when
        var invalidEmailResponse = 베이직_인증으로_내_회원_정보_조회_요청(INVALID_EMAIL, PASSWORD);
        var invalidPasswordResponse = 베이직_인증으로_내_회원_정보_조회_요청(ADMIN_EMAIL, INVALID_PASSWORD);

        // then
        회원_정보_조회_실패(invalidEmailResponse);
        회원_정보_조회_실패(invalidPasswordResponse);
    }

    @DisplayName("Session 로그인 후 내 정보 조회")
    @Test
    void myInfoWithSession() {
        // when
        var response = 폼_로그인_후_내_회원_정보_조회_요청(MEMBER_EMAIL, PASSWORD);

        // then
        회원_정보_조회됨(response, MEMBER_EMAIL, AGE);
    }

    @DisplayName("Session 로그인 실패")
    @Test
    void myInfoWithSession_Fail() {
        // when
        var invalidEmailResponse = 폼_로그인_후_내_회원_정보_조회_요청(INVALID_EMAIL, PASSWORD);
        var invalidPasswordResponse = 폼_로그인_후_내_회원_정보_조회_요청(ADMIN_EMAIL, INVALID_PASSWORD);

        // then
        회원_정보_조회_실패(invalidEmailResponse);
        회원_정보_조회_실패(invalidPasswordResponse);
    }

    @DisplayName("Bearer Auth로 내 정보 조회")
    @Test
    void myInfoWithBearerAuth() {
        // given
        String accessToken = 로그인_되어_있음(ADMIN_EMAIL, PASSWORD);

        // when
        var response = 베어러_인증으로_내_회원_정보_조회_요청(accessToken);

        // then
        회원_정보_조회됨(response, ADMIN_EMAIL, AGE);
    }

    @DisplayName("Bearer Auth 실패")
    @Test
    void myInfoWithBearerAuth_Fail() {
        // given
        String invalidToken = "asdfasdf";

        // when
        var invalidResponse = 베어러_인증으로_내_회원_정보_조회_요청(invalidToken);

        // then
        회원_정보_조회_실패(invalidResponse);
    }

    private ExtractableResponse<Response> 베이직_인증으로_내_회원_정보_조회_요청(String email, String password) {
        return RestAssured.given().log().all()
                .auth().preemptive().basic(email, password)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 폼_로그인_후_내_회원_정보_조회_요청(String email, String password) {
        return RestAssured.given().log().all()
                .auth().form(email, password,
                        new FormAuthConfig("/login/form", FORM_USERNAME_FIELD, FORM_PASSWORD_FIELD))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 베어러_인증으로_내_회원_정보_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    private void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertThat(response.jsonPath().getString("id")).isNotNull();
        assertThat(response.jsonPath().getString("email")).isEqualTo(email);
        assertThat(response.jsonPath().getInt("age")).isEqualTo(age);
    }

    private void 회원_정보_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
