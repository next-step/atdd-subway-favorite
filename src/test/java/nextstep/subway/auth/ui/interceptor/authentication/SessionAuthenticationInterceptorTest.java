package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.application.UserDetails;
import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SessionAuthenticationInterceptorTest {
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "test";

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private UserDetailsService userDetailsService;
    private SessionAuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        setSessionRequestHeader();

        userDetailsService = mock(CustomUserDetailsService.class);
        interceptor = new SessionAuthenticationInterceptor(userDetailsService);

    }

    void setSessionRequestHeader() {
        Map<String, String> params = new HashMap<>();
        params.put("username", EMAIL);
        params.put("password", PASSWORD);

        request.addParameters(params);
    }

    @DisplayName("Session 응답 테스트")
    @Test
    void returnSessionResponse() throws IOException {
        // given
        UserDetails loginMember = new LoginMember(1L, EMAIL, PASSWORD, 1);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(loginMember);

        // when
        interceptor.preHandle(request, response, new Object());

        UserDetails sessionMember = getLoginMemberInSession();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(sessionMember.getEmail()).isEqualTo(EMAIL);
        assertThat(sessionMember.getPassword()).isEqualTo(PASSWORD);
    }

    LoginMember getLoginMemberInSession() {
        SecurityContext context =  (SecurityContext)request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        Authentication authentication = context.getAuthentication();

        return (LoginMember) authentication.getPrincipal();
    }
}
