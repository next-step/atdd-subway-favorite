package nextstep.member.domain.exception;

import nextstep.common.exception.GlobalException;

public class InvalidTokenException extends GlobalException {
    public static final String MESSAGE = "유효하지 않은 토큰입니다.";

    public InvalidTokenException() {
        super(MESSAGE);
    }
}
