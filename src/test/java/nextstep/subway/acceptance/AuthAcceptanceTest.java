package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.베이직_인증으로_내_회원_정보_조회_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_정보_조회됨;


class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    @DisplayName("Basic Auth")
    @Test
    void myInfoWithBasicAuth() {
        ExtractableResponse<Response> response = 베이직_인증으로_내_회원_정보_조회_요청(EMAIL, PASSWORD);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Session 로그인 후 내 정보 조회")
    @Test
    void myInfoWithSession() {
        ExtractableResponse<Response> response = 폼_로그인_후_내_회원_정보_조회_요청(EMAIL, PASSWORD);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = 베어러_인증으로_내_회원_정보_조회_요청(accessToken);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    /**
     * Given & When 잘못된 토큰 정보로 Beerer 토큰 로그인을 하려하면
     * Then UNAUTHROZIED 응답이 온다.
     */
    @DisplayName("Bearer Auth - 잘못된 토큰으로 로그인")
    @Test
    void BearerAuth_fail_wrongToken() {
        // Given & When
        String wrongToken = "wrongToken";

        var response = 베어러_인증으로_내_회원_정보_조회_요청(wrongToken);

        // Then
        Assertions.assertThat(response.statusCode())
                .isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private ExtractableResponse<Response> 폼_로그인_후_내_회원_정보_조회_요청(String email, String password) {
        return RestAssured.given()
                .log()
                .all()
                .auth()
                .form(email, password, new FormAuthConfig("/login/form", "userName", "password"))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me")
                .then()
                .log()
                .all()
                .extract();
    }

    private ExtractableResponse<Response> 베어러_인증으로_내_회원_정보_조회_요청(String accessToken) {
        return RestAssured.given()
                .log()
                .all()
                .auth()
                .oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me")
                .then()
                .log()
                .all()
                .extract();
    }
}
