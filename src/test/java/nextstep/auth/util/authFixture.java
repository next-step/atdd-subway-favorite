package nextstep.auth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.UserDetails;
import nextstep.auth.UserDetailsService;
import nextstep.auth.authentication.AuthenticationInterceptor;
import nextstep.auth.token.TokenRequest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

public class authFixture {

    public static final Long DEFAULT_ID = 1L;
    public static final String DEFAULT_EMAIL = "email@email.com";
    public static final String DEFAULT_PASSWORD = "password";
    public static final int DEFAULT_AGE = 20;

    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    public static MockHttpServletRequest createMockRequest(final String email, final String password) throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(email, password);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    public static UserDetails createUserDetails(final Long id, final String email, final String password, final int age) {
        return new FakeUserDetails(id, email, password, age);
    }

    public static AuthenticationInterceptor createAuthenticationInterceptor(final UserDetailsService userDetailsService) {
        return new FakeAuthenticationInterceptor(userDetailsService);
    }

    private authFixture() {
    }
}
