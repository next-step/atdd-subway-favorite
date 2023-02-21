package nextstep.github.exception;

public class GithubException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "깃 허브 기능 처리 중 에러가 발생하였습니다.";

    public GithubException() {
        super(DEFAULT_MESSAGE);
    }

    public GithubException(String message) {
        super(message);
    }

    public GithubException(String message, Throwable cause) {
        super(message, cause);
    }

    public GithubException(Throwable cause) {
        super(cause);
    }

    public GithubException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
