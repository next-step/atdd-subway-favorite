package nextstep.subway.unit;

import nextstep.auth.user.UserDetail;
import nextstep.auth.user.UserDetailsService;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.member.domain.LoginMember;

import static org.mockito.Mockito.lenient;

public class InvalidAuthTarget {
  public static final String EMAIL = "newemail@email.com";
  public static final String PASSWORD = "passwordb";
  public static final UserDetail LOGIN_MEMBER = new LoginMember(2L, EMAIL, PASSWORD, 27);
  public static final AuthenticationToken AUTH_TOKEN = new AuthenticationToken(InvalidAuthTarget.EMAIL, InvalidAuthTarget.PASSWORD);

  public static void createMockLoginMember(UserDetailsService userDetailsService) {
    // given
    lenient().when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(LOGIN_MEMBER);
  }
}
