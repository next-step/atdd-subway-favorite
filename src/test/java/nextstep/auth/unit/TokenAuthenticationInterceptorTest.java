package nextstep.auth.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.User;
import nextstep.auth.authentication.token.TokenAuthenticationConverter;
import nextstep.auth.authentication.token.TokenAuthenticationInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import nextstep.support.MockRequest;
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
        ObjectMapper objectMapper = new ObjectMapper();
        TokenAuthenticationConverter converter = new TokenAuthenticationConverter(objectMapper);

        userDetailsService = mock(CustomUserDetailsService.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        interceptor = new TokenAuthenticationInterceptor(userDetailsService, converter, jwtTokenProvider, objectMapper);
        request = MockRequest.createTokenRequest(new TokenRequest(EMAIL, PASSWORD));
        response = new MockHttpServletResponse();
    }

    @Test
    void preHandle() throws Exception {
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 30));
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);

        interceptor.preHandle(request, response, new Object());

        tokenResponse_응답_검증();
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 AuthenticationException 발생")
    void authenticateInvalidPassword() {
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, "NEW_PASSWORD", 30));

        사용자_검증에_실패하면_에러_발생(interceptor, request, response, new Object());
    }

    @Test
    @DisplayName("사용자 정보가 없으면 AuthenticationException 발생")
    void authenticateUserNotFound() {
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(null);

        사용자_검증에_실패하면_에러_발생(interceptor, request, response, new Object());
    }

    private void 사용자_검증에_실패하면_에러_발생(TokenAuthenticationInterceptor interceptor, MockHttpServletRequest request, MockHttpServletResponse response, Object o) {
        assertThatThrownBy(() -> interceptor.preHandle(request, response, o))
            .isInstanceOf(AuthenticationException.class);
    }

    private void tokenResponse_응답_검증() throws UnsupportedEncodingException, JsonProcessingException {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }
}
