package nextstep.favorite.exception;

import nextstep.common.exception.NotFoundException;

public class FavoriteNotExistException extends NotFoundException {
    public FavoriteNotExistException(final Long id) {
        super(String.format("Favorite is not exist - id : %s", id));
    }
}
