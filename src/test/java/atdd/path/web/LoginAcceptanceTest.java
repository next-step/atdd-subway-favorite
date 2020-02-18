package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.TokenResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class LoginAcceptanceTest extends AbstractAcceptanceTest
{
    public static final String LOGIN_URL = "/login";

    private LoginHttpTest loginHttpTest;
    private MemberHttpTest memberHttpTest;

    @BeforeEach
    void setUp()
    {
        this.loginHttpTest = new LoginHttpTest(webTestClient);
        this.memberHttpTest = new MemberHttpTest(webTestClient);
    }

    @DisplayName("로그인 요청하여 토큰 응답 받기")
    @Test
    public void login()
    {
        // given
        Long memberId = memberHttpTest.createMember(MEMBER_EMAIL, MEMBER_NAME, MEMBER_PASSWORD);

        // when
        EntityExchangeResult<TokenResponseView> response = loginHttpTest.login(MEMBER_EMAIL, MEMBER_PASSWORD);

        // then
        assertThat(response.getResponseBody().getAccessToken()).isNotEmpty();
        assertThat(response.getResponseBody().getAccessToken()).isNotEmpty();

    }
}
