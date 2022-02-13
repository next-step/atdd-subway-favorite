package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.TokenRequest;
import nextstep.member.domain.LoginMember;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

public class AuthenticationFixture {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";
    public static final LoginMember FIXTURE_LOGIN_MEMBER = new LoginMember(1L, EMAIL, PASSWORD, 20);
    public static final ObjectMapper FIXTURE_OBJECT_MAPPER = new ObjectMapper();

    public static  MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(FIXTURE_OBJECT_MAPPER.writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}
