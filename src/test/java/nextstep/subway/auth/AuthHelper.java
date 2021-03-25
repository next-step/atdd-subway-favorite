package nextstep.subway.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import nextstep.subway.auth.dto.TokenRequest;
import org.springframework.mock.web.MockHttpServletRequest;

public class AuthHelper {

  public static final String EMAIL = "email@email.com";
  public static final String PASSWORD = "password";
  public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";
  public static final int AGE = 20;

  public static MockHttpServletRequest createMockRequest() throws IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addParameter("username", EMAIL);
    request.addParameter("password", PASSWORD);
    return request;
  }

  public static MockHttpServletRequest createTokenMockRequest() throws IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
    request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
    return request;
  }

}
