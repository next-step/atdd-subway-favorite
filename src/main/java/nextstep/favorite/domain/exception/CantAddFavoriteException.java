package nextstep.favorite.domain.exception;

public class CantAddFavoriteException extends RuntimeException {
    public CantAddFavoriteException(String message) {
        super(message);
    }
}
