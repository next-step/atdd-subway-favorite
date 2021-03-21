package nextstep.subway.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.dto.TokenRequest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

public class AuthRequestSteps {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    public static MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}
