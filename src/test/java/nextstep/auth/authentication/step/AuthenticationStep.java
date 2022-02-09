package nextstep.auth.authentication.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.TokenRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

public class AuthenticationStep {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    public static MockHttpServletRequest token_인증_요청_mock() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());

        return request;
    }

    public static MockHttpServletRequest session_인증_요청_mock() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("username", EMAIL);
        request.addParameter("password", PASSWORD);

        return request;
    }

    public static MockHttpServletResponse 인증_응답_mock() {
        return new MockHttpServletResponse();
    }
}
