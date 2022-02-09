package nextstep.auth.unit.authentication.session;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.session.SessionAuthenticationConverter;
import nextstep.auth.authentication.session.SessionAuthenticationInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SessionAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private SessionAuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        CustomUserDetailsService customUserDetailsService = mock(CustomUserDetailsService.class);

        LoginMember expectedMember = new LoginMember(-1L, EMAIL, PASSWORD, 0);
        Mockito.when(customUserDetailsService.loadUserByUsername(EMAIL)).thenReturn(expectedMember);

        interceptor = new SessionAuthenticationInterceptor(customUserDetailsService, new SessionAuthenticationConverter());
    }

    @Test
    void authenticate() {
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);

        Authentication authentication = interceptor.authenticate(authenticationToken);

        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        assertThat(loginMember.getEmail()).isEqualTo(EMAIL);
        assertThat(loginMember.getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    void preHandle() {
        HttpServletRequest request = createMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        interceptor.preHandle(request, response, null);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    private MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Map<String, String> params = new HashMap<>();
        params.put(SessionAuthenticationInterceptor.USERNAME_FIELD, EMAIL);
        params.put(SessionAuthenticationInterceptor.PASSWORD_FIELD, PASSWORD);
        request.addParameters(params);
        return request;
    }

}
