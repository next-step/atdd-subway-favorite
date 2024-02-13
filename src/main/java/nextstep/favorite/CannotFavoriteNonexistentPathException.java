package nextstep.favorite;

public class CannotFavoriteNonexistentPathException extends RuntimeException {
    public CannotFavoriteNonexistentPathException() {
        super("경로 조회가 불가능한 경우 즐겨찾기로 등록할 수 없습니다. ");
    }
}
