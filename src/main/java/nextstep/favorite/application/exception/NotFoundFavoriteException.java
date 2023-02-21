package nextstep.favorite.application.exception;

import nextstep.common.exception.ErrorCode;

public class NotFoundFavoriteException extends FavoriteException {

    public NotFoundFavoriteException(ErrorCode errorCode) {
        super(errorCode);
    }
}
