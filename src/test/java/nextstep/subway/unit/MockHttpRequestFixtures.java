package nextstep.subway.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.TokenRequest;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.MemberSteps.PASSWORD_FIELD;
import static nextstep.subway.acceptance.MemberSteps.USERNAME_FIELD;

public class MockHttpRequestFixtures {
    public static HttpServletRequest createSessionMockRequest(String email, String password) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Map<String, String> param = new HashMap<>();
        param.put(USERNAME_FIELD, email);
        param.put(PASSWORD_FIELD, password);
        request.addParameters(param);

        return request;
    }

    public static HttpServletRequest createTokenMockRequest(String email, String password) {
        MockHttpServletRequest request = new MockHttpServletRequest();

        TokenRequest tokenRequest = new TokenRequest(email, password);
        try {
            request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        } catch (JsonProcessingException ignore) {
        }

        return request;
    }

}
