package nextstep.subway.unit.auth;

import nextstep.auth.authentication.*;
import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.authentication.converter.SessionAuthenticationConverter;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpSession;
import java.io.IOException;

import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static nextstep.subway.unit.auth.AuthFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class SessionAuthenticationInterceptorTest {
    UserDetailsService userDetailsService;
    AuthenticationConverter converter;
    AuthenticationInterceptor interceptor;
    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @BeforeEach
    void setUp() throws IOException {
        userDetailsService = mock(CustomUserDetailsService.class);
        converter = new SessionAuthenticationConverter();
        interceptor = new SessionAuthenticationInterceptor(userDetailsService, converter);
        request = createSessionMockRequest();
        response = createMockResponse();
    }

    @Test
    void authenticate() {
        // given
        AuthenticationToken token = new AuthenticationToken(EMAIL, PASSWORD);
        given(userDetailsService.loadUserByUsername(EMAIL))
                .willReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));

        // when
        Authentication authentication = interceptor.authenticate(token);

        // then
        assertThat(authentication.getPrincipal()).isNotNull();
    }

    @Test
    void afterAuthentication() throws IOException {
        // given
        Authentication authentication = createAuthentication();

        // when
        interceptor.afterAuthentication(request, response, authentication);

        // then
        HttpSession session = request.getSession();
        SecurityContext securityContext = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(securityContext.getAuthentication()).isEqualTo(authentication);
    }

    @Test
    void preHandle() throws Exception {
        // given
        given(userDetailsService.loadUserByUsername(EMAIL))
                .willReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        HttpSession session = request.getSession();
        SecurityContext securityContext = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        LoginMember loginMember = (LoginMember) securityContext.getAuthentication().getPrincipal();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(loginMember.getEmail()).isEqualTo(EMAIL);
        assertThat(loginMember.getPassword()).isEqualTo(PASSWORD);
    }
}