package nextstep.subway.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.dto.TokenRequest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

public class AuthSteps {
    public static MockHttpServletRequest createMockRequest(String email, String pasword) throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(email, pasword);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}
