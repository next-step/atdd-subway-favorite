package nextstep.member.domain.exception;

public class CantAddFavoriteException extends RuntimeException {
    public CantAddFavoriteException(String message) {
        super(message);
    }
}
