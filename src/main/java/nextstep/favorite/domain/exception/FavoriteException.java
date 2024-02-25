package nextstep.favorite.domain.exception;

public class FavoriteException extends RuntimeException {
    private static String DEFAULT_MESSAGE = "즐겨찾기 에러 : ";
    public FavoriteException() {
        super(DEFAULT_MESSAGE);
    }

    public FavoriteException(String message) {
        super(DEFAULT_MESSAGE + message);
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
