package nextstep.auth.unit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.interceptor.SessionAuthenticationInterceptor;
import nextstep.auth.authentication.interceptor.TokenAuthenticationInterceptor;
import nextstep.auth.authentication.converter.SessionAuthenticationConverter;
import nextstep.auth.authentication.converter.TokenAuthenticationConverter;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpSession;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static nextstep.auth.unit.model.AuthenticationUnitTestHelper.EMAIL;
import static nextstep.auth.unit.model.AuthenticationUnitTestHelper.createMockSessionRequest;
import static nextstep.auth.unit.model.AuthenticationUnitTestHelper.createMockTokenRequest;
import static nextstep.auth.unit.model.AuthenticationUnitTestHelper.getAuthenticationToken;
import static nextstep.auth.unit.model.AuthenticationUnitTestHelper.getUserDetails;
import static nextstep.auth.unit.model.AuthenticationUnitTestHelper.getPayload;
import static nextstep.auth.unit.TokenGenerator.getToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ExtendWith(MockitoExtension.class)
class AuthenticationInterceptorTest {

    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private TokenAuthenticationConverter tokenAuthenticationConverter;
    @Mock
    private SessionAuthenticationConverter sessionAuthenticationConverter;
    @Spy
    private ObjectMapper objectMapper;
    @InjectMocks
    private TokenAuthenticationInterceptor tokenAuthenticationInterceptorNew;
    @InjectMocks
    private SessionAuthenticationInterceptor sessionAuthenticationInterceptorNew;
    private LoginMember userDetails;
    private AuthenticationToken authenticationToken;
    private MockHttpServletRequest request;

    @BeforeEach
    void setFixtures() {
        userDetails = getUserDetails();
        when(customUserDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);

        authenticationToken = getAuthenticationToken();
    }

    @Test
    void tokenPreHandle() throws Exception {
        request = createMockTokenRequest();
        when(tokenAuthenticationConverter.convert(request)).thenReturn(authenticationToken);
        when(jwtTokenProvider.createToken(getPayload(userDetails))).thenReturn(getToken());

        MockHttpServletResponse response = new MockHttpServletResponse();
        tokenAuthenticationInterceptorNew.preHandle(request, response, null);

        assertThat(response.getStatus()).isEqualTo(SC_OK);
        assertThat(response.getContentType()).isEqualTo(APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(TokenResponse.of(getToken())));
    }

    @Test
    void sessionPreHandle() throws Exception {
        request = createMockSessionRequest();
        when(sessionAuthenticationConverter.convert(request)).thenReturn(authenticationToken);

        MockHttpServletResponse response = new MockHttpServletResponse();
        sessionAuthenticationInterceptorNew.preHandle(request, response, null);

        HttpSession session = request.getSession();
        SecurityContext securityContext = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        assertThat(response.getStatus()).isEqualTo(SC_OK);
        assertThat(securityContext.getAuthentication().getPrincipal()).isEqualTo(userDetails);
    }
}
