package nextstep.common.exception;

public class LoginException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "로그인 기능 처리 중 에러가 발생하였습니다.";

    public LoginException() {
        super(DEFAULT_MESSAGE);
    }

    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginException(Throwable cause) {
        super(cause);
    }

    public LoginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
