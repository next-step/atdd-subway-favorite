package nextstep.favorite.domain.exception;

public class FavoriteConflictException extends FavoriteException {
    public static String DEFAULT_MESSAGE = "즐겨찾기가 중복됩니다.";
    public FavoriteConflictException() {
        super(DEFAULT_MESSAGE);
    }

    public FavoriteConflictException(String message) {
        super(message);
    }
}
