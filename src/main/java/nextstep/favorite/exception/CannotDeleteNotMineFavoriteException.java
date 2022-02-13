package nextstep.favorite.exception;

public class CannotDeleteNotMineFavoriteException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "자신의 즐겨찾기만 삭제 가능합니다";

    public CannotDeleteNotMineFavoriteException() {
        super(DEFAULT_MESSAGE);
    }
}
