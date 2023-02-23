package nextstep.member.domain.exception;

import nextstep.common.exception.GlobalException;

public class MemberNotFoundException extends GlobalException {
    private static final String MESSAGE = "유저를 찾을 수 업습니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }
}
