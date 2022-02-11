package nextstep.subway.unit.service;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.converter.SessionAuthenticationConverter;
import nextstep.auth.authentication.interceptor.SessionAuthenticationInterceptor;
import nextstep.auth.context.SecurityContext;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpSession;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static nextstep.subway.unit.AuthenticationUnitTestHelper.EMAIL;
import static nextstep.subway.unit.AuthenticationUnitTestHelper.createMockSessionRequest;
import static nextstep.subway.unit.AuthenticationUnitTestHelper.getAuthenticationToken;
import static nextstep.subway.unit.AuthenticationUnitTestHelper.getUserDetails;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionAuthenticationInterceptorTest {

    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private SessionAuthenticationConverter sessionAuthenticationConverter;
    @InjectMocks
    private SessionAuthenticationInterceptor sessionAuthenticationInterceptorNew;
    private LoginMember userDetails;
    private AuthenticationToken authenticationToken;

    @BeforeEach
    void setFixtures() {
        userDetails = getUserDetails();
        when(customUserDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        authenticationToken = getAuthenticationToken();
    }

    @Test
    void preHandle() throws Exception {
        MockHttpServletRequest request = createMockSessionRequest();
        when(sessionAuthenticationConverter.convert(request)).thenReturn(authenticationToken);

        MockHttpServletResponse response = new MockHttpServletResponse();
        sessionAuthenticationInterceptorNew.preHandle(request, response, null);

        HttpSession session = request.getSession();
        SecurityContext securityContext = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        assertThat(response.getStatus()).isEqualTo(SC_OK);
        assertThat(securityContext.getAuthentication().getPrincipal()).isEqualTo(userDetails);
    }
}
