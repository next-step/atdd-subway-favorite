package nextstep.subway.applicaion.favorite.exception;

public class InvalidFavoriteOwnerException extends FavoriteException {
    public InvalidFavoriteOwnerException() {
        super("사용자의 즐겨찾기가 아닙니다.");
    }
}
