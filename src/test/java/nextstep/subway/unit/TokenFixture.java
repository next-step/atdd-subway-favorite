package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import nextstep.auth.token.TokenRequest;
import org.springframework.mock.web.MockHttpServletRequest;

public class TokenFixture {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    public static MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    public static MockHttpServletRequest createSessionMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Map<String, String[]> paramMap = new HashMap<>();
        paramMap.put("username", new String[] { EMAIL });
        paramMap.put("password", new String[] { PASSWORD });
        request.setParameters(paramMap);
        return request;
    }
}
