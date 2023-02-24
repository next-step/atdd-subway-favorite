package nextstep.member.domain.exception;

import nextstep.common.exception.GlobalException;

public class BadCredentialException extends GlobalException {

    public static final String MESSAGE = "유저를 찾을 수 업습니다.";

    public BadCredentialException() {
        super(MESSAGE);
    }
}
