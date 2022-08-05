package nextstep.auth.exception;

import nextstep.common.BusinessException;

public class UnauthorizedException extends BusinessException {

    public static final String MESSAGE = "권한이 없는 사용자 입니다";

    public UnauthorizedException() {
        super(MESSAGE);
    }
}
