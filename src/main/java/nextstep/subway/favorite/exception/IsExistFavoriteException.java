package nextstep.subway.favorite.exception;

public class IsExistFavoriteException extends RuntimeException {
    public static String MESSAGE = "이미 존재하는 즐겨찾기입니다.";

    public IsExistFavoriteException() {
        super(MESSAGE);
    }
}
