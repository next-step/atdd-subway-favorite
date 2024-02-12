package nextstep.favorite.exception;

import nextstep.common.exception.BadRequestException;

public class FavoriteSaveException extends BadRequestException {
    public FavoriteSaveException(final String message) {
        super(message);
    }
}
