package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.adapter.in.UserDetailsService;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.token.TokenRequest;
import nextstep.member.domain.LoginMember;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static nextstep.auth.authentication.SessionAuthenticationInterceptor.PASSWORD_FIELD;
import static nextstep.auth.authentication.SessionAuthenticationInterceptor.USERNAME_FIELD;
import static org.mockito.Mockito.lenient;

public class AuthTarget {
  public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";
  public static final String EMAIL = "email@email.com";
  public static final String PASSWORD = "password";
  public static final LoginMember LOGIN_MEMBER = new LoginMember(1L, EMAIL, PASSWORD, 25);
  public static final AuthenticationToken AUTH_TOKEN = new AuthenticationToken(EMAIL, PASSWORD);


  public static void createMockLoginMember(UserDetailsService userDetailsService) {
    // given
    lenient().when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(LOGIN_MEMBER);
  }

  public static MockHttpServletRequest createSessionMockRequest() throws IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    Map<String, String> param = new HashMap<>();
    param.put(USERNAME_FIELD, EMAIL);
    param.put(PASSWORD_FIELD, PASSWORD);
    request.addParameters(param);
    return request;
  }

  public static MockHttpServletRequest createTokenMockRequest() throws IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
    request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
    return request;
  }

  public static MockHttpServletRequest createInvalidMockRequest() throws IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    TokenRequest tokenRequest = new TokenRequest(EMAIL, "teststeststestst");
    request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
    return request;
  }

}
