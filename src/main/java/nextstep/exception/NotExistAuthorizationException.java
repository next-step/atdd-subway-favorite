package nextstep.exception;

import nextstep.error.ErrorCode;

public class NotExistAuthorizationException extends NextStepException {
    public NotExistAuthorizationException() {
        super(ErrorCode.NOT_EXIST_AUTHORIZATION_ERROR);
    }
}
