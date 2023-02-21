package nextstep.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.member.acceptance.MemberSteps.베어러_인증_로그인_요청_성공;
import static nextstep.member.acceptance.MemberSteps.베어러_인증_로그인_요청_실패;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 29;

    // given
    @BeforeEach
    protected void setUp() {
        super.setUp();

        회원_생성_요청(EMAIL, PASSWORD, AGE);
    }

    /**
     * given 회원이 있을 때
     * when 회원의 이메일과 비밀번호로 로그인 요청하면
     * then 해당 회원에 대한 토큰을 발급받는다.
     */
    @DisplayName("이메일과 비밀번호로 토큰을 요청한다.")
    @Test
    void bearerAuthLogin() {
        // when
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청_성공(EMAIL, PASSWORD);

        // then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * given 회원이 있을 때
     * when 회원의 이메일과 다른 이메일로 로그인 요청하면
     * then 토큰을 발급받을 수 없다.
     */
    @DisplayName("일치하지 않는 이메일로 로그인 요청하면 토큰을 발급받을 수 없다.")
    @Test
    void bearerAuthLoginExceptionNotMatchEmail() {
        // given
        final String invalidEmail = "admin1@email.com";

        // when & then
        베어러_인증_로그인_요청_실패(invalidEmail, PASSWORD);
    }

    /**
     * given 회원이 있을 때
     * when 회원의 비밀번호와 다른 비밀번호로 로그인 요청하면
     * then 토큰을 발급받을 수 없다.
     */
    @DisplayName("Bearer Auth : 일치하지 않는 비밀번호로 로그인 요청하면 토큰을 발급받을 수 없다.")
    @Test
    void bearerAuthLoginExceptionNotMatchPassword() {
        // given
        final String invalidPassword = "password1";

        // when & then
        베어러_인증_로그인_요청_실패(EMAIL, invalidPassword);
    }
}