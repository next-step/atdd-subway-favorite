package nextstep.subway.acceptance.auth;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.auth.AuthSteps.*;
import static nextstep.subway.acceptance.member.MemberSteps.*;


class AuthAcceptanceTest extends AcceptanceTest {
    private static final Integer AGE = 20;

    @DisplayName("Basic Auth")
    @Test
    void myInfoWithBasicAuth() {
        ExtractableResponse<Response> response = 베이직_인증으로_내_회원_정보_조회_요청(ADMIN_EMAIL, ADMIN_PASSWORD);

        회원_정보_조회됨(response, ADMIN_EMAIL, AGE);
    }

    @DisplayName("Basic Auth")
    @Test
    void myInfoWithBasicAuthInvalidToken() {
        ExtractableResponse<Response> response = 베이직_인증으로_내_회원_정보_조회_요청(ADMIN_EMAIL, ADMIN_PASSWORD);

        회원_정보_조회됨(response, ADMIN_EMAIL, AGE);
    }

    @DisplayName("Session 로그인 후 내 정보 조회")
    @Test
    void myInfoWithSession() {
        ExtractableResponse<Response> response = 폼_로그인_후_내_회원_정보_조회_요청(ADMIN_EMAIL, ADMIN_PASSWORD);

        회원_정보_조회됨(response, ADMIN_EMAIL, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        String accessToken = 로그인_되어_있음(ADMIN_EMAIL, ADMIN_PASSWORD);

        ExtractableResponse<Response> response = 베어러_인증으로_내_회원_정보_조회_요청(accessToken);

        회원_정보_조회됨(response, ADMIN_EMAIL, AGE);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithBearerAuthInvalidToken() {
        String invalidToken = "invalidToken";

        ExtractableResponse<Response> response = 베어러_인증으로_내_회원_정보_조회_요청(invalidToken);

        인증_예외_발생(response);
    }

}
