package nextstep.favorite.application.exception;

import nextstep.common.exception.ErrorCode;

public class FavoriteCreateException extends FavoriteException {

    public FavoriteCreateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
