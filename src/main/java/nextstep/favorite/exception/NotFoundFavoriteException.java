package nextstep.favorite.exception;

import nextstep.common.BusinessException;

public class NotFoundFavoriteException extends BusinessException {

    public static final String MESSAGE = "해당 즐겨찾기를 찾을 수 없습니다";

    public NotFoundFavoriteException() {
        super(MESSAGE);
    }
}
