package nextstep.auth.exception;

import nextstep.support.error.ApiException;
import nextstep.support.error.ErrorCode;

public class AuthenticationException extends ApiException {
  public AuthenticationException() {
    super(ErrorCode.UNAUTHORIZED);
  }
}
