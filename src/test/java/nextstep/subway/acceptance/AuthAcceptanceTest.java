package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.MemberSteps.*;


class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    @BeforeEach
    void setUp_() {
        회원_생성_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Basic Auth")
    @Test
    void myInfoWithBasicAuth() {
        ExtractableResponse<Response> response = 베이직_인증으로_내_회원_정보_조회_요청(EMAIL, PASSWORD);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Session 로그인 후 내 정보 조회")
    @Test
    void myInfoWithSession() {
        var 회원_정보 = 폼_로그인_후_내_회원_정보_조회_요청(EMAIL, PASSWORD);

        회원_정보_조회됨(회원_정보, EMAIL, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        var 인증토큰 = 로그인_되어_있음(EMAIL, PASSWORD);

        var 회원_정보 = 베어러_인증으로_내_회원_정보_조회(인증토큰);

        회원_정보_조회됨(회원_정보, EMAIL, AGE);
    }

    private ExtractableResponse<Response> 폼_로그인_후_내_회원_정보_조회_요청(String email, String password) {
        return 세션_인증으로_내_회원_정보_조회_요청(email, password);
    }

    private ExtractableResponse<Response> 베어러_인증으로_내_회원_정보_조회(String accessToken) {
        return 베어러_토큰_인증으로_내_회원_정보_조회_요청(accessToken);
    }
}
