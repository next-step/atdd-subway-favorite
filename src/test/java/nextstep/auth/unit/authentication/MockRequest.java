package nextstep.auth.unit.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.session.SessionAuthenticationConverter;
import nextstep.auth.token.TokenRequest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MockRequest {
    public static MockHttpServletRequest createSessionRequest(String username, String password) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Map<String, String> params = new HashMap<>();
        params.put(SessionAuthenticationConverter.USERNAME_FIELD, username);
        params.put(SessionAuthenticationConverter.PASSWORD_FIELD, password);
        request.addParameters(params);
        return request;
    }

    public static MockHttpServletRequest createTokenRequest(String username, String password) throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(username, password);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}
