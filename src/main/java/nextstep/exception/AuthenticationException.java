package nextstep.exception;

import nextstep.error.ErrorCode;

public class AuthenticationException extends NextStepException {
    public AuthenticationException() {
        super(ErrorCode.UNAUTHORIZED);
    }
}
