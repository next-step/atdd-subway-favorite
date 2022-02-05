package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.convert.AuthenticationConverter;
import nextstep.auth.authentication.convert.TokenAuthenticationConverter;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("토큰 인증 기능 단위 테스트")
class TokenAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    TokenAuthenticationInterceptor tokenAuthenticationInterceptor;

    @Mock
    CustomUserDetailsService userDetailsService;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    AuthenticationConverter tokenAuthenticationConverter;

    AuthenticationToken mockAuthenticationToken;

    @BeforeEach
    void setUp() {
        tokenAuthenticationConverter = new TokenAuthenticationConverter();
        tokenAuthenticationInterceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider, tokenAuthenticationConverter);
        mockAuthenticationToken = createMockAuthenticationToken();
    }

    @Test
    void authenticate() {
        // given
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(getMockLoginMember(EMAIL, PASSWORD));

        // when
        Authentication authenticate = tokenAuthenticationInterceptor.authenticate(mockAuthenticationToken);
        LoginMember principal = (LoginMember) authenticate.getPrincipal();

        // then
        assertThat(principal).isNotNull();
        assertThat(principal).extracting(LoginMember::getEmail).isEqualTo(EMAIL);
        assertThat(principal).extracting(LoginMember::getPassword).isEqualTo(PASSWORD);
    }

    @DisplayName("인증 실패 - email 찾을 수 없음")
    @Test
    void authenticateNotFoundEmail() {
        // given
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(null);

        // when, then
        assertThatThrownBy(() -> tokenAuthenticationInterceptor.authenticate(mockAuthenticationToken))
                .isInstanceOf(AuthenticationException.class);

    }

    @DisplayName("인증 실패 - password 불일치")
    @Test
    void authenticateIncorrectPassword() {
        // given
        String INCORRECT_PASSWORD = PASSWORD + "-incorrect";

        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(getMockLoginMember(EMAIL, INCORRECT_PASSWORD));

        // when, then
        assertThatThrownBy(() -> tokenAuthenticationInterceptor.authenticate(mockAuthenticationToken))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    void preHandle() throws Exception {
        // given
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(getMockLoginMember(EMAIL, PASSWORD));
        when(jwtTokenProvider.createToken(anyString()))
                .thenReturn(JWT_TOKEN);

        // when
        MockHttpServletResponse mockResponse = createMockResponse();
        tokenAuthenticationInterceptor.preHandle(createMockRequest(), mockResponse, new Object());

        // then
        assertThat(mockResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(getContent(mockResponse)).isEqualTo(new TokenResponse(JWT_TOKEN));
    }

    private TokenResponse getContent(MockHttpServletResponse mockResponse) throws Exception {
        return new ObjectMapper().readValue(mockResponse.getContentAsString(), TokenResponse.class);
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    private MockHttpServletResponse createMockResponse() {
        return new MockHttpServletResponse();
    }

    private LoginMember getMockLoginMember(String email, String password) {
        return new LoginMember(0L, email, password, 0);
    }

    private AuthenticationToken createMockAuthenticationToken() {
        return new AuthenticationToken(EMAIL, PASSWORD);
    }

}
