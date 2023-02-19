package nextstep.subway.acceptance.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.member.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.member.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    /**
     * Given 회원가입을 하고
     * When 로그인을 요청하면
     * Then 토큰을 발급받는다
     */
    @DisplayName("Bearer Auth Token 발급")
    @Test
    void bearerAuth() {
        회원_생성_요청(EMAIL, PASSWORD, 29);

        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * Given 회원가입을 하고
     * When 올바르지 않은 아이디 또는 패스워드를 입력하면
     * Then 토큰을 받을 수 없다.
     */
    @DisplayName("Bearer Auth Token 발급")
    @Test
    void bearerAuthException() {
        회원_생성_요청(EMAIL, PASSWORD, 29);

        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}
