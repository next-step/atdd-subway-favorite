package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.member.application.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class TokenAuthenticationInterceptorTest {
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private TokenAuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        CustomUserDetailsService customUserDetailsService = mock(CustomUserDetailsService.class);
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);

        interceptor = new TokenAuthenticationInterceptor(customUserDetailsService, jwtTokenProvider, new ObjectMapper());
    }

    @Test
    void convert() throws IOException {
        HttpServletRequest request = createMockRequest();

        AuthenticationToken authenticationToken = interceptor.convert(request);

        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @Test
    void authenticate() {
    }

    @Test
    void preHandle() throws IOException {
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

}
