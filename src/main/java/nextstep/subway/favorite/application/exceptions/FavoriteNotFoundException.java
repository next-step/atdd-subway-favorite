package nextstep.subway.favorite.application.exceptions;

public final class FavoriteNotFoundException extends RuntimeException {
    public FavoriteNotFoundException(String message) {
        super(message);
    }
}
