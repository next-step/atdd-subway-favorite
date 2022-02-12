package nextstep.subway.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.TokenRequest;
import org.springframework.mock.web.MockHttpServletRequest;

public class MockRequest {
    public static MockHttpServletRequest createTokenRequest(TokenRequest tokenRequest) throws JsonProcessingException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}
