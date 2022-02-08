package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.TokenRequest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

public final class TokenAuthenticationUnitTestHelper {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";

    public static MockHttpServletRequest createMockTokenRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}
