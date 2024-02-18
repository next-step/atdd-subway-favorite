package nextstep.favorite.exception;

import nextstep.common.exception.BadRequestException;

public class FavoriteCreationException extends BadRequestException {
    public FavoriteCreationException(final String message) {
        super(message);
    }
}
