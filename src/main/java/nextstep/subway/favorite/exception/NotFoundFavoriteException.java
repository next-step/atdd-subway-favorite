package nextstep.subway.favorite.exception;

public class NotFoundFavoriteException extends RuntimeException {
    public static String MESSAGE = "존재하지 않는 즐겨찾기에 대한 요청입니다.";

    public NotFoundFavoriteException() {
        super(MESSAGE);
    }
}
