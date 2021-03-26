package nextstep.subway.favorite.exception;

import nextstep.subway.common.exception.ExistResourceException;

public class FavoriteAlreadyExistException extends ExistResourceException {

    private static final String EXCEPTION_MESSAGE_EXIST_FAVORITE = "해당 즐겨찾기가 이미 존재합니다.";

    public FavoriteAlreadyExistException() {
        super(EXCEPTION_MESSAGE_EXIST_FAVORITE);
    }
}
