package nextstep.member.exception;

import nextstep.common.exception.NotFoundException;

public class MemberNotFoundException extends NotFoundException {
    public MemberNotFoundException(final String email) {
        super(String.format("Member is not found - email : %s", email));
    }
}
