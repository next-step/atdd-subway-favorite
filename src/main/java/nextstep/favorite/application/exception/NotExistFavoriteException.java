package nextstep.favorite.application.exception;

public class NotExistFavoriteException extends IllegalStateException {
    private static final String MESSAGE = "존재하지 않는 즐겨찾기입니다.";

    public NotExistFavoriteException() {
        super(MESSAGE);
    }
}
