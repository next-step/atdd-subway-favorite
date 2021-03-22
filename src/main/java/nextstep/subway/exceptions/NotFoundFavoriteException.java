package nextstep.subway.exceptions;

public class NotFoundFavoriteException extends RuntimeException {
    public static final String DEFAULT_MSG = "해당 즐겨찾기는 존재하지 않습니다.";

    public NotFoundFavoriteException() {
        super(DEFAULT_MSG);
    }

    public NotFoundFavoriteException(String message) {
        super(message);
    }
}
