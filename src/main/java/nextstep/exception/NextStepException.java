package nextstep.exception;

import nextstep.error.ErrorCode;

public class NextStepException extends RuntimeException {

    private final ErrorCode errorCode;

    public NextStepException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public NextStepException(final ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
