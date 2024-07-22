package nextstep.member.exception;

import nextstep.support.error.ApiException;
import nextstep.support.error.ErrorCode;

public class AuthorizationException extends ApiException {
  public AuthorizationException() {
    super(ErrorCode.FORBIDDEN);
  }
}
