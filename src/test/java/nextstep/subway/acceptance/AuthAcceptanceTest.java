package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.acceptance.AdministratorInfo.*;
import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;


class AuthAcceptanceTest extends AcceptanceTest {

    String accessToken;
    @BeforeEach
    void authSetUp(){
        accessToken = 로그인_되어_있음(ADMIN_EMAIL, ADMIN_PASSWORD);
    }

    @DisplayName("Basic Auth")
    @Test
    void myInfoWithBasicAuth() {
        ExtractableResponse<Response> response = 베이직_인증으로_내_회원_정보_조회_요청(ADMIN_EMAIL, ADMIN_PASSWORD);

        회원_정보_조회됨(response, ADMIN_EMAIL, ADMIN_AGE);
    }

    @DisplayName("Session 로그인 후 내 정보 조회")
    @Test
    void myInfoWithSession() {
        ExtractableResponse<Response> response = 폼_로그인_후_내_회원_정보_조회_요청(ADMIN_EMAIL, ADMIN_PASSWORD);

        회원_정보_조회됨(response, ADMIN_EMAIL, ADMIN_AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
//        String accessToken = 로그인_되어_있음(ADMIN_EMAIL, ADMIN_PASSWORD);

        ExtractableResponse<Response> response = 베어러_인증으로_내_회원_정보_조회_요청(accessToken);

        회원_정보_조회됨(response, ADMIN_EMAIL, ADMIN_AGE);
    }

    @DisplayName("유효하지 않은 토큰으로 인한 Bearer Auth 실패.")
    @Test
    void myInfoWithBearerAuthFail() {
//        String accessToken = 로그인_되어_있음(ADMIN_EMAIL, ADMIN_PASSWORD);
        String invalidToken = "invalidToken";
        ExtractableResponse<Response> response = 베어러_인증으로_내_회원_정보_조회_요청(invalidToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private ExtractableResponse<Response> 폼_로그인_후_내_회원_정보_조회_요청(String email, String password) {
        return RestAssured.given().log().all()
                .auth().form(email, password
                        , new FormAuthConfig("/login/form", USERNAME_FIELD, ADMIN_PASSWORD_FIELD))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 베어러_인증으로_내_회원_정보_조회_요청(String accessToken) {
        return RestAssured.given().log().all().
                auth().oauth2(accessToken).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/members/me").
                then().log().all().
                extract();
    }
}
