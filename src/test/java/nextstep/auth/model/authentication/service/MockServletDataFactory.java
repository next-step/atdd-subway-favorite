package nextstep.auth.model.authentication.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.model.token.dto.TokenRequest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

public class MockServletDataFactory {

    public static final String MOCK_EMAIL = "login@email.com";
    public static final String MOCK_PASSWORD = "password";

    public static MockHttpServletRequest createMockRequest(ObjectMapper objectMapper) throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(MOCK_EMAIL, MOCK_PASSWORD);
        request.setContent(objectMapper.writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}
