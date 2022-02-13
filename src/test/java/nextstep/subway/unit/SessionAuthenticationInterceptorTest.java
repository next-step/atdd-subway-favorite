package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationInterceptor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthenticationTokenConverter;
import nextstep.auth.authentication.SessionAuthenticationInterceptor;
import nextstep.auth.authentication.SessionAuthenticationTokenConverter;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SessionAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    @Test
    void convert() throws IOException {
        // given
        AuthenticationTokenConverter converter = new SessionAuthenticationTokenConverter();
        MockHttpServletRequest request = createMockRequest();

        // when
        AuthenticationToken authenticationToken = converter.convert(request);

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @Test
    void afterAuthentication() throws IOException {
        // given
        CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);
        AuthenticationInterceptor interceptor = new SessionAuthenticationInterceptor(userDetailsService);

        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, AGE));

        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);
        Authentication authentication = interceptor.authenticate(authenticationToken);

        MockHttpServletRequest request = createMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        interceptor.afterAuthentication(request, response, authentication);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        SecurityContext securityContext = (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        LoginMember loginMember = (LoginMember) securityContext.getAuthentication().getPrincipal();
        assertThat(loginMember).isEqualTo(new LoginMember(1L, EMAIL, PASSWORD, AGE));
    }

    private MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("username", EMAIL);
        request.addParameter("password", PASSWORD);
        return request;
    }

}
