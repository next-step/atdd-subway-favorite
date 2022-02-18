package nextstep.auth.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.member.domain.LoginMember;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static nextstep.auth.unit.AuthData.*;

public class MockRequest {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter(USERNAME_FIELD, EMAIL);
        request.addParameter(PASSWORD_FIELD, PASSWORD);

        return request;
    }

    public static MockHttpServletRequest createMockRequestWithToken() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(OBJECT_MAPPER.writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    public static MockHttpServletRequest createMockRequestWithInvalidAccessToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalidAccessToken");

        return request;
    }

    public static MockHttpServletRequest createMockRequestWithValidAccessToken(JwtTokenProvider jwtTokenProvider) throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();

        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, AGE);
        String payload = OBJECT_MAPPER.writeValueAsString(loginMember);
        String accessToken = jwtTokenProvider.createToken(payload);
        request.addHeader("Authorization", "Bearer " + accessToken);

        return request;
    }
}
