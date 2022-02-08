package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.domain.LoginMember;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

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

    public static LoginMember getUserDetails() {
        return new LoginMember(1L, EMAIL, PASSWORD, 20);
    }

    public static String getPayload(LoginMember userDetails) throws IOException {
        return new ObjectMapper().writeValueAsString(userDetails);
    }

    public static String getAccessToken(MockHttpServletResponse response) throws IOException {
        TokenResponse tokenResponse = new ObjectMapper().readValue(response.getContentAsString(), TokenResponse.class);
        return tokenResponse.getAccessToken();
    }
}
