package nextstep.favorite.exception;

public class NotOwnerFavoriteException extends RuntimeException{
    public NotOwnerFavoriteException(String message) {
        super(message);
    }
}
