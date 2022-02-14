package nextstep.subway.unit.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authorization.strategy.SecurityContextHolderStrategy;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.TokenRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

public class AuthFixture {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 30;
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    public static MockHttpServletRequest createTokenMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    public static MockHttpServletRequest createSessionMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter(USERNAME_FIELD, EMAIL);
        request.addParameter(PASSWORD_FIELD, PASSWORD);
        return request;
    }

    public static MockHttpServletResponse createMockResponse() {
        return new MockHttpServletResponse();
    }

    public static Authentication createAuthentication() {
        AuthenticationToken token = new AuthenticationToken(EMAIL, PASSWORD);
        return new Authentication(token);
    }

    public static SecurityContextHolderStrategy createSecurityContextHolderStrategy() {
        return new FakeSecurityContextHolderStrategy();
    }
}
