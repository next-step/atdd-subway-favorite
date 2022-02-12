package nextstep.auth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.UserDetails;
import nextstep.auth.UserDetailsService;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AuthFixture {

    public static final String DEFAULT_EMAIL = "email@email.com";
    public static final String DEFAULT_PASSWORD = "password";

    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    public static HttpServletRequest createMockRequest(final String email, final String password) throws IOException {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(email, password);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    public static UserDetails createUserDetails(final String password) {
        return new FakeUserDetails(password);
    }

    public static UserDetailsService createUserDetailsService(final String password) {
        return new FakeUserDetailsService(password);
    }

    public static JwtTokenProvider createJwtTokenProvider() {
        return new FakeJwtTokenProvider();
    }

    private AuthFixture() {
    }
}
