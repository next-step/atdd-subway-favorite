package nextstep.favorite.application.exceptions;

public class FavoriteNotFoundException extends RuntimeException {
    public FavoriteNotFoundException(long favoriteId) {
        super(String.valueOf(favoriteId));
    }
}
