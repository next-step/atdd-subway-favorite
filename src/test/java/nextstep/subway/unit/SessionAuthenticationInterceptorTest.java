package nextstep.subway.unit;

import nextstep.auth.authentication.session.SessionAuthenticationInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import nextstep.subway.support.MockRequest;
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
    private static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";
    private CustomUserDetailsService userDetailsService;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private SessionAuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        userDetailsService = mock(CustomUserDetailsService.class);
        interceptor = new SessionAuthenticationInterceptor(userDetailsService);
        request = MockRequest.crateSessionRequest(EMAIL, PASSWORD);
        response = new MockHttpServletResponse();
    }

    @Test
    void preHandle() {
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));

        interceptor.preHandle(request, response, new Object());

        sessionResponse_응답_검증();
    }

    private void sessionResponse_응답_검증() {
        SecurityContext attribute = (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        Authentication authentication = attribute.getAuthentication();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(authentication.getPrincipal()).isEqualTo(new LoginMember(1L, EMAIL, PASSWORD, 20));
    }
}
