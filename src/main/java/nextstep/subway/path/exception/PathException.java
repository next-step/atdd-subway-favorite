package nextstep.subway.path.exception;

public class PathException extends RuntimeException {
    public PathException() {
    }

    public PathException(String message) {
        super(message);
    }

    public PathException(String message, Throwable cause) {
        super(message, cause);
    }

    public PathException(Throwable cause) {
        super(cause);
    }

    public PathException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
