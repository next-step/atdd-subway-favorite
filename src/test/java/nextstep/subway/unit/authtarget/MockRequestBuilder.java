package nextstep.subway.unit.authtarget;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.TokenRequest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static nextstep.auth.authentication.SessionConverter.PASSWORD_FIELD;
import static nextstep.auth.authentication.SessionConverter.USERNAME_FIELD;

public class MockRequestBuilder {
  private MockRequestBuilder() {}

  public static MockHttpServletRequest createSessionMockRequest(String email, String password) throws IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    Map<String, String> param = new HashMap<>();
    param.put(USERNAME_FIELD, email);
    param.put(PASSWORD_FIELD, password);
    request.addParameters(param);
    return request;
  }

  public static MockHttpServletRequest createTokenMockRequest(String email, String password) throws IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    TokenRequest tokenRequest = new TokenRequest(email, password);
    request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
    return request;
  }
}
