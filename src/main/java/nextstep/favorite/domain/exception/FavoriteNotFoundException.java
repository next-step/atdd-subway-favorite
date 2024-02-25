package nextstep.favorite.domain.exception;

public class FavoriteNotFoundException extends FavoriteException {
    public static String DEFAULT_MESSAGE = "즐겨찾기가 존재하지 않습니다.";
    public FavoriteNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public FavoriteNotFoundException(String message) {
        super(message);
    }
}
