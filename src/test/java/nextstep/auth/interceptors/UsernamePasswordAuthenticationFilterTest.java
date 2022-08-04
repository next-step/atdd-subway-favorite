package nextstep.auth.interceptors;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.member.application.LoginMemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static nextstep.DataLoader.MEMBER_EMAIL;
import static nextstep.DataLoader.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UsernamePasswordAuthenticationFilterTest {
    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";

    @Autowired
    private LoginMemberService loginMemberService;
    private AuthenticationProvidingInterceptor authenticationInterceptor;

    @BeforeEach
    void setUp() {
        authenticationInterceptor = new UsernamePasswordAuthenticationFilter(loginMemberService);
    }

    @Test
    void convert() throws IOException {
        // when
        AuthenticationToken token = authenticationInterceptor.convert(createFormRequest());

        // then
        assertThat(token.getPrincipal()).isEqualTo(MEMBER_EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    private MockHttpServletRequest createFormRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(USERNAME_FIELD, MEMBER_EMAIL);
        request.setParameter(PASSWORD_FIELD, PASSWORD);
        return request;
    }
}