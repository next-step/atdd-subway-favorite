package nextstep.favorite.exception;

public enum FavoriteErrorMessage {
	NOT_FOUND_FAVORITE("즐겨찾기를 찾을 수 없습니다."),
	SHOULD_BE_OWN_FAVORITES("내 즐겨찾기이어야 합니다.");

	private String message;

	FavoriteErrorMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
