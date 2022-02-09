package nextstep.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.TokenRequest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

public class authFixture {

    public static final Long DEFAULT_ID = 1L;
    public static final int DEFAULT_AGE = 1;
    public static final String DEFAULT_EMAIL = "email@email.com";
    public static final String DEFAULT_PASSWORD = "password";

    public static MockHttpServletRequest createMockRequest(final String email, final String password) throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(email, password);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    private authFixture() {
    }
}
