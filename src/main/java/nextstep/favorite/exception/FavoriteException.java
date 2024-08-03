package nextstep.favorite.exception;

public class FavoriteException extends RuntimeException{
    public FavoriteException() {
        super();
    }

    public FavoriteException(String message) {
        super(message);
    }

    public FavoriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public FavoriteException(Throwable cause) {
        super(cause);
    }

    protected FavoriteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
