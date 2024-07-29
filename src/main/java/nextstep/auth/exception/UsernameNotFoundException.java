package nextstep.auth.exception;

import nextstep.support.error.ApiException;
import nextstep.support.error.ErrorCode;

public class UsernameNotFoundException extends ApiException {
  public UsernameNotFoundException(String username) {
    super(ErrorCode.UNAUTHORIZED, String.format("Username %s 이 존재하지 ", username));
  }
}
