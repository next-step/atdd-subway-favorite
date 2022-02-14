package nextstep.subway.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.TokenRequest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

public class MockRequest {

    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";

    public static MockHttpServletRequest createMockTokenRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    public static MockHttpServletRequest createMockSessionRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(USERNAME_FIELD, EMAIL);
        request.setParameter(PASSWORD_FIELD, PASSWORD);
        return request;
    }
}
