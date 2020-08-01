package nextstep.subway.favorite.application;

public class FavoriteNotFoundException extends RuntimeException {

    public FavoriteNotFoundException() {
        super("favorite is not found.");
    }
}
