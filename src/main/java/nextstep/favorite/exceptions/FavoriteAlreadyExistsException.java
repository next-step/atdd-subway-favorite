package nextstep.favorite.exceptions;

import nextstep.exceptions.BaseException;
import nextstep.exceptions.ErrorMessage;

public class FavoriteAlreadyExistsException extends BaseException {
    public FavoriteAlreadyExistsException(final ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
