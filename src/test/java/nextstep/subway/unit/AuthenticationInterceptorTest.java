package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.*;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    @DisplayName("Token 요청을 AuthenticationToken 으로 변환 테스트")
    @Test
    void tokenConvert() throws IOException {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        AuthenticationConverter tokenAuthenticationConverter = new TokenAuthenticationConverter(objectMapper);
        MockHttpServletRequest request = createJwtMockRequest();

        // when
        AuthenticationToken authenticationToken = tokenAuthenticationConverter.convert(request);

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @DisplayName("FormLogin 요청을 AuthenticationToken 으로 변환 테스트")
    @Test
    void sessionConvert() throws IOException {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        AuthenticationConverter sessionAuthenticationConverter = new SessionAuthenticationConverter();
        MockHttpServletRequest request = createSessionMockRequest();

        // when
        AuthenticationToken authenticationToken = sessionAuthenticationConverter.convert(request);

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @DisplayName("AuthenticationToken 를 Authentication 로 변환 테스트")
    @Test
    void authenticate() {
        // given
        CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        ObjectMapper objectMapper = new ObjectMapper();
        AuthenticationConverter tokenAuthenticationConverter = new TokenAuthenticationConverter(objectMapper);
        AfterAuthentication tokenAfterAuthentication = new TokenAfterAuthentication(jwtTokenProvider, objectMapper);
        AuthenticationInterceptor interceptor =
                new AuthenticationInterceptor(tokenAuthenticationConverter, userDetailsService, tokenAfterAuthentication);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));

        // when
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);
        Authentication authentication = interceptor.authenticate(authenticationToken);

        // then
        assertThat(authentication.getPrincipal()).isNotNull();
    }

    @DisplayName("Token 요청 후처리 테스트")
    @Test
    void tokenInterceptorPreHandle() throws IOException {
        // given
        CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        ObjectMapper objectMapper = new ObjectMapper();
        TokenAuthenticationConverter tokenAuthenticationConverter = new TokenAuthenticationConverter(objectMapper);
        TokenAfterAuthentication tokenAfterAuthentication = new TokenAfterAuthentication(jwtTokenProvider, objectMapper);
        AuthenticationInterceptor interceptor =
                new AuthenticationInterceptor(tokenAuthenticationConverter, userDetailsService, tokenAfterAuthentication);

        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);

        // when
        MockHttpServletRequest request = createJwtMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        interceptor.preHandle(request, response, new Object());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }

    @DisplayName("Session 요청 후처리 테스트")
    @Test
    void sessionInterceptorPreHandle() throws IOException {
        // given
        CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);
        AuthenticationConverter sessionAuthenticationConverter = new SessionAuthenticationConverter();
        SessionAfterAuthentication sessionAfterAuthentication = new SessionAfterAuthentication();
        AuthenticationInterceptor authenticationInterceptor =
                new AuthenticationInterceptor(sessionAuthenticationConverter, userDetailsService, sessionAfterAuthentication);

        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 20);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(loginMember);

        // when
        MockHttpServletRequest request = createSessionMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        authenticationInterceptor.preHandle(request, response, new Object());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        SecurityContext securityContext = (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        LoginMember principal = (LoginMember) securityContext.getAuthentication().getPrincipal();
        assertThat(principal).isEqualTo(loginMember);
    }

    private MockHttpServletRequest createJwtMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    private MockHttpServletRequest createSessionMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(SessionAuthenticationConverter.USERNAME_FIELD, EMAIL);
        request.setParameter(SessionAuthenticationConverter.PASSWORD_FIELD, PASSWORD);
        return request;
    }
}