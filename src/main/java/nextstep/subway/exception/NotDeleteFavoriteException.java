package nextstep.subway.exception;

public class NotDeleteFavoriteException extends IllegalArgumentException {

    private static final String MESSAGE = "즐겨찾기 소유자가 아니여서 삭제할 수 없습니다. memberId=%d";

    public NotDeleteFavoriteException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
