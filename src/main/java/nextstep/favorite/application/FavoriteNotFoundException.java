package nextstep.favorite.application;

public class FavoriteNotFoundException extends RuntimeException {
    public FavoriteNotFoundException(long favoriteId) {
        super(String.valueOf(favoriteId));
    }
}
