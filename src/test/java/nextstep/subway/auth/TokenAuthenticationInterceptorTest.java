package nextstep.subway.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.token.TokenAuthenticationInterceptor;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("TokenAuthenticationInterceptor 관련 테스트")
class TokenAuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    private MockHttpServletRequest request;
    private TokenAuthenticationInterceptor interceptor;
    private CustomUserDetailsService userDetailsService;
    private JwtTokenProvider tokenProvider;

    @BeforeEach
    void setUp() throws IOException {
        request = createMockRequest();
        userDetailsService = mock(CustomUserDetailsService.class);
        tokenProvider = mock(JwtTokenProvider.class);
        interceptor = new TokenAuthenticationInterceptor(userDetailsService, tokenProvider);
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
        when(userDetailsService.loadUserByUsername(authenticationToken.getPrincipal()))
                .thenReturn(new LoginMember(null, EMAIL, PASSWORD, AGE));

        // when
        Authentication authenticate = interceptor.authenticate(authenticationToken);

        // then
        assertThat(((LoginMember) authenticate.getPrincipal()).getEmail()).isEqualTo(EMAIL);
        assertThat(((LoginMember) authenticate.getPrincipal()).getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    void preHandle() throws IOException {
    }

    private void AuthenticationToken_요청됨(AuthenticationToken token) {
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    private AuthenticationToken AuthenticationToken_요청() throws IOException {
        return interceptor.convert(request);
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

}
