package nextstep.subway.domain.exceptions;

public class CanNotDeleteFavoriteException extends RuntimeException {
    public CanNotDeleteFavoriteException(String message) {
        super(message);
    }
}
