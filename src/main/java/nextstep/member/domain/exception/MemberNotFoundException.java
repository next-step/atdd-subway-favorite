package nextstep.member.domain.exception;

import nextstep.common.exception.exception.EntityNotFoundException;

public class MemberNotFoundException extends EntityNotFoundException {
    public MemberNotFoundException() {
        super("회원이 존재하지 않습니다.");
    }
}
