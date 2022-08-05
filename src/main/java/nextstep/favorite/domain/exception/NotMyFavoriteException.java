package nextstep.favorite.domain.exception;

public class NotMyFavoriteException extends RuntimeException {
    public NotMyFavoriteException(String message) {
        super(message);
    }
}
