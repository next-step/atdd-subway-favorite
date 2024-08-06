package nextstep.favorite.exceptions;

import nextstep.exceptions.BaseException;
import nextstep.exceptions.ErrorMessage;

public class FavoriteNotFoundException extends BaseException {
    public FavoriteNotFoundException(final ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
