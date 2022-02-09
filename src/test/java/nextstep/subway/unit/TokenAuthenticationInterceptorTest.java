package nextstep.subway.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    private CustomUserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private TokenAuthenticationInterceptor interceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() throws IOException {
        userDetailsService = mock(CustomUserDetailsService.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        interceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider);
        request = createMockRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("httpRequest로 AuthenticationToken 생성")
    void convert() throws IOException {
        AuthenticationToken token = interceptor.convert(request);

        AuthenticationToken_생성_검증(token);
    }

    @Test
    @DisplayName("로그인 사용자 검증")
    void authenticate() {
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);
        TokenAuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));

        Authentication authenticate = interceptor.authenticate(authenticationToken);

        authenticate_사용자_검증(authenticate);
    }

    @Test
    void preHandle() throws IOException {
        TokenAuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider);

        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);

        interceptor.preHandle(request, response, new Object());

        tokenResponse_응답_검증();
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 AuthenticationException 발생")
    void authenticateInvalidPassword() {
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);
        TokenAuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, "NEW_PASSWORD", 20));

        사용자_검증에_실패하면_에러_발생(authenticationToken, interceptor);
    }

    @Test
    @DisplayName("사용자 정보가 없으면 AuthenticationException 발생")
    void authenticateUserNotFound() {
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);
        TokenAuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(null);

        사용자_검증에_실패하면_에러_발생(authenticationToken, interceptor);
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    private void AuthenticationToken_생성_검증(AuthenticationToken token) {
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    private void authenticate_사용자_검증(Authentication authenticate) {
        assertThat(authenticate.getPrincipal()).isEqualTo(new LoginMember(1L, EMAIL, PASSWORD, 20));
    }

    private void 사용자_검증에_실패하면_에러_발생(AuthenticationToken authenticationToken, TokenAuthenticationInterceptor interceptor) {
        assertThatThrownBy(() -> interceptor.authenticate(authenticationToken))
            .isInstanceOf(AuthenticationException.class);
    }

    private void tokenResponse_응답_검증() throws UnsupportedEncodingException, JsonProcessingException {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }
}
