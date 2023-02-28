package nextstep.member.domain.exception;

import nextstep.common.exception.GlobalException;

public class FavoriteIsNotYoursException extends GlobalException {

    private static final String MESSAGE = "자기 자신의 즐겨찾기만 삭제할 수 있습니다.";

    public FavoriteIsNotYoursException() {
        super(MESSAGE);
    }
}
