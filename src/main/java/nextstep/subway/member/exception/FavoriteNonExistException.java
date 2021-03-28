package nextstep.subway.member.exception;

import nextstep.subway.common.exception.NonExistResourceException;

public class FavoriteNonExistException extends NonExistResourceException {

    private static final String EXCEPTION_MESSAGE_NON_EXIST_FAVORITE = "해당 즐겨찾기가 존재하지 않습니다.";

    public FavoriteNonExistException() {
        super(EXCEPTION_MESSAGE_NON_EXIST_FAVORITE);
    }
}
