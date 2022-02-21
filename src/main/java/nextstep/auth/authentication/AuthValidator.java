package nextstep.auth.authentication;

import nextstep.auth.adapter.in.UserDetail;

public class AuthValidator {

  public static void validateAuthentication(UserDetail userDetails, String credentials) {

    if (userDetails == null) {
      throw new AuthenticationException();
    }

    if (!userDetails.checkPassword(credentials)) {
      throw new AuthenticationException();
    }
  }
}
