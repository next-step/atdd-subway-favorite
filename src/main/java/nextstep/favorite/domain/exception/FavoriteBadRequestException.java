package nextstep.favorite.domain.exception;

public class FavoriteBadRequestException extends FavoriteException {
    private static String DEFAULT_MESSAGE = "잘못된 즐겨찾기 요청입니다.";
    public FavoriteBadRequestException() {
        super(DEFAULT_MESSAGE);
    }

    public FavoriteBadRequestException(String message) {
        super(message);
    }
}
