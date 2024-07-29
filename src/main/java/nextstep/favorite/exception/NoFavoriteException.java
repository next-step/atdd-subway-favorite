package nextstep.favorite.exception;

import nextstep.favorite.common.FavoriteErrorMessage;

public class NoFavoriteException extends RuntimeException {

    public NoFavoriteException(FavoriteErrorMessage favoriteErrorMessage) {
        super(favoriteErrorMessage.getMessage());
    }
}
