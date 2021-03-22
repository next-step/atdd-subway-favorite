package nextstep.subway.auth;

import nextstep.subway.auth.application.SessionAuthenticationConverter;
import nextstep.subway.auth.application.TokenAuthenticationConverter;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.ui.session.SessionAuthenticationInterceptor;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
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

    @Test
    void authenticate() {
        CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);
        SessionAuthenticationInterceptor interceptor = new SessionAuthenticationInterceptor(userDetailsService);

        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));

        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);
        Authentication authentication = interceptor.authenticate(authenticationToken);

        assertThat(authentication.getPrincipal()).isNotNull();
    }

    @Test
    void preHandle() throws IOException {
        CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);
        SessionAuthenticationInterceptor interceptor = new SessionAuthenticationInterceptor(userDetailsService);

        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));

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
