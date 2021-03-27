package nextstep.subway.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.token.TokenAuthenticationInterceptor;
import nextstep.subway.member.application.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("TokenAuthenticationInterceptor 관련 테스트")
class TokenAuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    private MockHttpServletRequest request;
    private TokenAuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() throws IOException {
        request = createMockRequest();
        interceptor = new TokenAuthenticationInterceptor(
                mock(CustomUserDetailsService.class),
                mock(JwtTokenProvider.class));
    }

    @DisplayName("HttpRequest를 AuthenticationToken으로 변환한다")
    @Test
    void convert() throws IOException {
        // when
        AuthenticationToken token = AuthenticationToken_요청();

        // then
        AuthenticationToken_요청됨(token);
    }

    @Test
    void authenticate() {
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
