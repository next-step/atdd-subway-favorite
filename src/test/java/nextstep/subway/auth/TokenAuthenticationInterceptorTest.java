package nextstep.subway.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.AuthenticationConverter;
import nextstep.subway.auth.infrastructure.UserDetails;
import nextstep.subway.auth.ui.token.TokenAuthenticationConverter;
import nextstep.subway.auth.ui.token.TokenAuthenticationInterceptor;
import nextstep.subway.exception.InvalidPasswordException;
import nextstep.subway.exception.UserDetailsNotExistException;
import nextstep.subway.member.application.CustomUserDetailsService;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@DisplayName("TokenAuthenticationInterceptor 관련 테스트")
class TokenAuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final String WRONG_PASSWORD = "wrongPassword";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    private MockHttpServletRequest request;
    private TokenAuthenticationInterceptor interceptor;
    private CustomUserDetailsService userDetailsService;
    private JwtTokenProvider tokenProvider;
    private AuthenticationConverter authenticationConverter;

    @BeforeEach
    void setUp() throws IOException {
        request = createMockRequest();
        userDetailsService = mock(CustomUserDetailsService.class);
        tokenProvider = mock(JwtTokenProvider.class);
        authenticationConverter = new TokenAuthenticationConverter();
        interceptor = new TokenAuthenticationInterceptor(userDetailsService, tokenProvider, authenticationConverter);
    }

    @DisplayName("HttpRequest를 AuthenticationToken으로 변환한다")
    @Test
    void convert() throws IOException {
        // when
        AuthenticationToken token = AuthenticationToken_요청();

        // then
        AuthenticationToken_요청됨(token);
    }

    @DisplayName("Authentication 객체를 생성한다")
    @Test
    void authenticate() throws IOException {
        // given
        AuthenticationToken authenticationToken = AuthenticationToken_요청();
        회원_저장되어있음(UserDetails.of(1L, EMAIL, PASSWORD));

        // when
        Authentication authenticate = Authentication_요청(authenticationToken);

        // then
        Authentication_요청됨(authenticate);
    }

    @DisplayName("회원정보가 없을 때, Authentication 객체를 생성한다")
    @Test
    void authenticateWhenUserNonExist() throws IOException {
        // given
        AuthenticationToken authenticationToken = AuthenticationToken_요청();
        회원_저장되어있음(null);

        // when
        assertThatThrownBy(() -> Authentication_요청(authenticationToken))
                .isInstanceOf(UserDetailsNotExistException.class);
    }

    @DisplayName("비밀번호가 다를 때, Authentication 객체를 생성한다")
    @Test
    void authenticateWhenUseWrongPassword() throws IOException {
        // given
        AuthenticationToken authenticationToken = AuthenticationToken_요청();
        회원_저장되어있음(UserDetails.of(1L, EMAIL, WRONG_PASSWORD));

        // when
        assertThatThrownBy(() -> Authentication_요청(authenticationToken))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @DisplayName("JWT를 생성하여 응답으로 보낸다")
    @Test
    void preHandle() throws IOException {
        // given
        MockHttpServletRequest request = createMockRequest();
        MockHttpServletResponse response = createMockResponse();
        회원_저장되어있음(UserDetails.of(1L, EMAIL, PASSWORD));
        when(tokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);
        assertThat(response.getContentType()).isEqualTo(APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }

    private void 회원_저장되어있음( UserDetails userDetails) {
        when(userDetailsService.loadUserByUsername(EMAIL))
                .thenReturn(userDetails);
    }

    private void Authentication_요청됨(Authentication authenticate) {
        assertThat(((UserDetails) authenticate.getPrincipal()).getPrincipal()).isEqualTo(EMAIL);
        assertThat(((UserDetails) authenticate.getPrincipal()).getCredential()).isEqualTo(PASSWORD);
    }

    private void AuthenticationToken_요청됨(AuthenticationToken token) {
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    private Authentication Authentication_요청(AuthenticationToken authenticationToken) {
        return interceptor.authenticate(authenticationToken);
    }

    private AuthenticationToken AuthenticationToken_요청() throws IOException {
        return authenticationConverter.convert(request);
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    private MockHttpServletResponse createMockResponse() throws IOException {
        return new MockHttpServletResponse();
    }
}
