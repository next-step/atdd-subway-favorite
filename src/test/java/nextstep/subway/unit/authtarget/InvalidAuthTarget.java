package nextstep.subway.unit.authtarget;

import nextstep.auth.user.UserDetail;
import nextstep.auth.user.UserDetailsService;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.member.domain.LoginMember;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static nextstep.auth.authentication.SessionConverter.PASSWORD_FIELD;
import static nextstep.auth.authentication.SessionConverter.USERNAME_FIELD;
import static org.mockito.Mockito.lenient;

public class InvalidAuthTarget {
  public static final String EMAIL = "newemail@email.com";
  public static final String PASSWORD = "passwordb";
  public static final int AGE = 27;
  public static final UserDetail LOGIN_MEMBER = new LoginMember(2L, EMAIL, PASSWORD, AGE);
  public static final AuthenticationToken AUTH_TOKEN = new AuthenticationToken(InvalidAuthTarget.EMAIL, InvalidAuthTarget.PASSWORD);

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
