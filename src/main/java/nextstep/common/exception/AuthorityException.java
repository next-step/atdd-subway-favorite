package nextstep.common.exception;

public class AuthorityException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "리소스 권한이 존재하지 않습니다.";

    public AuthorityException() {
        super(DEFAULT_MESSAGE);
    }

    public AuthorityException(String message) {
        super(message);
    }

    public AuthorityException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorityException(Throwable cause) {
        super(cause);
    }

    public AuthorityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
