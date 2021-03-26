package nextstep.subway.auth;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.ui.session.SessionAuthenticationInterceptor;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.Objects;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SessionAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    UserDetailsService userDetailsService;
    SessionAuthenticationInterceptor interceptor;

    @BeforeEach
    public void setUp() {
        userDetailsService = mock(UserDetailsService.class);
        interceptor = new SessionAuthenticationInterceptor(userDetailsService);
    }

    @Test
    void authenticate() {
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(LoginMember.of(1L, EMAIL, PASSWORD, 20));

        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);
        Authentication authentication = interceptor.authenticate(authenticationToken);

        assertThat(authentication.getPrincipal()).isNotNull();
    }

    @Test
    void preHandle() throws IOException {
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(LoginMember.of(1L, EMAIL, PASSWORD, 20));

        MockHttpServletRequest request = createMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        interceptor.preHandle(request, response, new Object());

        assertThat(Objects.requireNonNull(request.getSession()).getAttribute(SPRING_SECURITY_CONTEXT_KEY)).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("username", EMAIL);
        request.setParameter("password", PASSWORD);
        return request;
    }

}