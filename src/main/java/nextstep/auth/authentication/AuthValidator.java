package nextstep.auth.authentication;

import nextstep.member.domain.LoginMember;

public class AuthValidator {

  public static void validateAuthentication(LoginMember userDetails, String credentials) {

    if (userDetails == null) {
      throw new AuthenticationException();
    }

    if (!userDetails.checkPassword(credentials)) {
      throw new AuthenticationException();
    }
  }
}
