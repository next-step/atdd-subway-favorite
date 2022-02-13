package nextstep.auth.model.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.model.token.dto.TokenRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.HashMap;
import java.util.Map;

import static nextstep.auth.model.authentication.interceptor.SessionAuthenticationInterceptor.PASSWORD_FIELD;
import static nextstep.auth.model.authentication.interceptor.SessionAuthenticationInterceptor.USERNAME_FIELD;

public class MockServletDataFactory {
    public static final String MOCK_EMAIL = "login@email.com";
    public static final String MOCK_PASSWORD = "password";
    public static final int MOCK_AGE = 26;

    public static MockHttpServletRequest createSessionMockRequest() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();

        Map<String, String> params = new HashMap<>();
        params.put(USERNAME_FIELD, MOCK_EMAIL);
        params.put(PASSWORD_FIELD, MOCK_PASSWORD);
        mockRequest.setParameters(params);

        return mockRequest;
    }

    public static MockHttpServletRequest createTokenMockRequest(ObjectMapper objectMapper) throws JsonProcessingException {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(MOCK_EMAIL, MOCK_PASSWORD);
        mockRequest.setContent(objectMapper.writeValueAsString(tokenRequest).getBytes());

        return mockRequest;
    }

    public static MockHttpServletRequest createMockRequest(ObjectMapper objectMapper) {
        return new MockHttpServletRequest();
    }

    public static MockHttpServletResponse createMockResponse() {
        return new MockHttpServletResponse();
    }
}
