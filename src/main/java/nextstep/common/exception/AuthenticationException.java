package nextstep.common.exception;

public class AuthenticationException extends RuntimeException {
    private final ErrorCode errorCode;

    public AuthenticationException() {
        super(ErrorCode.UNAUTHORIZED.getMessage());
        this.errorCode = ErrorCode.UNAUTHORIZED;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
