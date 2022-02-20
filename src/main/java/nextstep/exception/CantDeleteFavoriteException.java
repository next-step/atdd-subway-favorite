package nextstep.exception;

public class CantDeleteFavoriteException extends RuntimeException {
	private static final String MESSAGE = "즐겨찾기를 삭제할 수 없습니다.";

	public CantDeleteFavoriteException() {
		super(MESSAGE);
	}
}
