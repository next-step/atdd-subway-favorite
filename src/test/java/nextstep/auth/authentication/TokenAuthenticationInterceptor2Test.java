package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptor2Test {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    private TokenAuthenticationInterceptor2 interceptor;

    @BeforeEach
    void setUp() throws IOException {
        interceptor = new TokenAuthenticationInterceptor2(customUserDetailsService, jwtTokenProvider, new TokenAuthenticationConverter());
    }

    @DisplayName("AuthenticationToken을 이용해 Authentication을 생성한다")
    @Test
    void authenticate() {
        // given
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);
        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 10);
        when(customUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(loginMember);

        // when
        Authentication authenticate = interceptor.authenticate(authenticationToken);

        // then
        assertThat(authenticate.getPrincipal()).isInstanceOf(LoginMember.class);
        assertThat(authenticate.getPrincipal()).isNotNull();
    }

    @DisplayName("회원 정보가 없으면 Authentication 생성을 실패한다")
    @Test
    void authenticate_fail() {
        // given
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);
        LoginMember loginMember = new LoginMember(1L, "mail", "notMatch", 10);
        when(customUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(loginMember);

        // then
        assertThatThrownBy(() -> interceptor.authenticate(authenticationToken))
                .isInstanceOf(AuthenticationException.class);
    }

    @DisplayName("토큰을 생성하고 응답한다")
    @Test
    void preHandle() throws IOException {
        // given
        MockHttpServletRequest 요청 = mock_요청생성();
        MockHttpServletResponse 응답 = mock_응답생성();
        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 10);
        when(customUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(loginMember);
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);

        // when
        boolean result = interceptor.preHandle(요청, 응답, new Object());

        // then
        assertThat(result).isFalse();
        assertThat(응답.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(응답.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(응답.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }

    private MockHttpServletRequest mock_요청생성() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());

        return request;
    }

    private MockHttpServletResponse mock_응답생성() {
        return new MockHttpServletResponse();
    }

}