package nextstep.auth.unit;

import nextstep.auth.authentication.UserDetails;
import nextstep.auth.authentication.session.SessionAuthenticationConverter;
import nextstep.auth.authentication.session.SessionAuthenticationInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.support.MockRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SessionAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private CustomUserDetailsService userDetailsService;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private SessionAuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {

        userDetailsService = mock(CustomUserDetailsService.class);
        interceptor = new SessionAuthenticationInterceptor(userDetailsService, new SessionAuthenticationConverter());
        request = MockRequest.crateSessionRequest(EMAIL, PASSWORD);
        response = new MockHttpServletResponse();
    }

    @Test
    void preHandle() throws Exception {
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new UserDetails(1L, EMAIL, PASSWORD));

        interceptor.preHandle(request, response, new Object());

        sessionResponse_응답_검증();
    }

    private void sessionResponse_응답_검증() {
        SecurityContext attribute = (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        Authentication authentication = attribute.getAuthentication();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(authentication.getPrincipal()).isEqualTo(new UserDetails(1L, EMAIL, PASSWORD));
    }
}
