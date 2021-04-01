package nextstep.subway.exception;

public class NotExistsFavoriteException extends RuntimeException{
    public NotExistsFavoriteException() {
        super();
    }

    public NotExistsFavoriteException(String message) {
        super(message);
    }

    public NotExistsFavoriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistsFavoriteException(Throwable cause) {
        super(cause);
    }

    protected NotExistsFavoriteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
