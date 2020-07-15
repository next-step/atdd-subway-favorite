package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpSession;
import java.io.IOException;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UsernamePasswordAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    private UsernamePasswordAuthenticationInterceptor interceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, AGE));
        interceptor = new UsernamePasswordAuthenticationInterceptor(userDetailsService);

        request = createMockRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void preHandle() throws IOException {
        // when
        interceptor.preHandle(request, response, new Object());

        // then
        HttpSession httpSession = request.getSession();
        assertThat(httpSession.getAttribute(SPRING_SECURITY_CONTEXT_KEY)).isNotNull();
    }

    @Test
    void convert() {
        // when
        AuthenticationToken authenticationToken = interceptor.convert(request);

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @Test
    void attemptAuthentication() {
        // when
        Authentication authentication = interceptor.attemptAuthentication(request, response);

        // then
        assertThat(((LoginMember) authentication.getPrincipal()).getEmail()).isEqualTo(EMAIL);
        assertThat(((LoginMember) authentication.getPrincipal()).getPassword()).isEqualTo(PASSWORD);
    }

    private MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        request.setParameter("username", EMAIL);
        request.setParameter("password", PASSWORD);
        return request;
    }
}