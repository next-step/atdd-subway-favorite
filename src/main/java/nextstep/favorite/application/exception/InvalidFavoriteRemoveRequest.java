package nextstep.favorite.application.exception;

import nextstep.common.exception.ErrorCode;

public class InvalidFavoriteRemoveRequest extends FavoriteException {

    public InvalidFavoriteRemoveRequest(ErrorCode errorCode) {
        super(errorCode);
    }
}
