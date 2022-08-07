package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.MemberSteps.*;

class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final String WRONG_PASSWORD = "wrong_password";
    private static final Integer AGE = 20;

    @DisplayName("Basic Auth 로그인 후 내 정보 조회")
    @Test
    void myInfoWithBasicAuth() {
        ExtractableResponse<Response> response = 베이직_인증으로_내_회원_정보_조회_요청(EMAIL, PASSWORD);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("form 로그인 후 내 정보 조회")
    @Test
    void myInfoWithForm() {
        ExtractableResponse<Response> response = 폼_로그인_후_내_회원_정보_조회_요청(EMAIL, PASSWORD);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Bearer Auth 로그인 후 내 정보 조회")
    @Test
    void myInfoWithBearerAuth() {
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = 베어러_인증으로_내_회원_정보_조회_요청(accessToken);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Basic Auth 로그인 실패")
    @Test
    void basicAuthLoginFail() {
        ExtractableResponse<Response> response = 베이직_인증으로_내_회원_정보_조회_요청(EMAIL, WRONG_PASSWORD);

        로그인_실패(response);
    }

    @DisplayName("form 로그인 실패")
    @Test
    void formLoginFail() {
        ExtractableResponse<Response> response = 폼_로그인_후_내_회원_정보_조회_요청(EMAIL, WRONG_PASSWORD);

        로그인_실패(response);
    }
}
