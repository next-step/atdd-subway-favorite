package nextstep.member.domain.exception;

import nextstep.common.exception.exception.EntityNotFoundException;

public class MemberNotFoundException extends EntityNotFoundException {
    public static final String ERROR_MESSAGE = "존재하지 않는 회원입니다. Email=%s";

    public MemberNotFoundException(String email) {
        super(String.format(ERROR_MESSAGE, email));
    }
}
