package nextstep.common.exception;

public class NoPathException extends RuntimeException {
    private final ErrorCode errorCode;

    public NoPathException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
