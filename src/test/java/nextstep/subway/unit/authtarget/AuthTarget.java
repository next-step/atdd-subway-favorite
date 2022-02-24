package nextstep.subway.unit.authtarget;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.user.UserDetail;
import nextstep.auth.user.UserDetailsService;
import nextstep.member.domain.LoginMember;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static org.mockito.Mockito.lenient;

public class AuthTarget {
  public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";
  public static final String EMAIL = "email@email.com";
  public static final String PASSWORD = "password";
  public static final UserDetail LOGIN_MEMBER = new LoginMember(1L, EMAIL, PASSWORD, 25);
  public static final AuthenticationToken AUTH_TOKEN = new AuthenticationToken(EMAIL, PASSWORD);


  public static void createMockLoginMember(UserDetailsService userDetailsService) {
    // given
    lenient().when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(LOGIN_MEMBER);
  }

  public static MockHttpServletRequest createSessionMockRequest() throws IOException {
    return MockRequestBuilder.createSessionMockRequest(EMAIL, PASSWORD);
  }

  public static MockHttpServletRequest createTokenMockRequest() throws IOException {
    return MockRequestBuilder.createTokenMockRequest(EMAIL, PASSWORD);
  }

}
