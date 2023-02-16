package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.MemberSteps.*;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 10;

    @DisplayName("Bearer Auth에 성공한다.")
    @Test
    void bearerAuth() {

        회원_생성_요청(EMAIL, PASSWORD, AGE);

        final ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        인증_로그인_응답_성공(response);
    }

    @DisplayName("패스워드를 잘못 입력해서 Bearer Auth 인증에 실패한다.")
    @Test
    void error_bearerAuth() {

        회원_생성_요청(EMAIL, PASSWORD, AGE);

        final ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, "different password");

        인증_로그인_응답_실패(response);
    }
}