package nextstep.favorite.exception;

public class CannotDeleteFavorite extends RuntimeException {
	public CannotDeleteFavorite(FavoriteErrorMessage message) {
		super(message.getMessage());
	}
}
