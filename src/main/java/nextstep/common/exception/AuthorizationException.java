package nextstep.common.exception;

public class AuthorizationException extends RuntimeException {
    private final ErrorCode errorCode;

    public AuthorizationException() {
        super(ErrorCode.FORBIDDEN.getMessage());
        this.errorCode = ErrorCode.FORBIDDEN;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
