package nextstep.subway.auth.session;

import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SessionFixture {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    public static MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(USERNAME_FIELD, EMAIL);
        paramMap.put(PASSWORD_FIELD, PASSWORD);
        request.setParameters(paramMap);
        return request;
    }
}
