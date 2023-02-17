package nextstep.member.config.exception;

import nextstep.member.config.message.MemberError;

public class PasswordMatchException extends RuntimeException {

    public PasswordMatchException(final MemberError memberError) {
        super(memberError.getMessage());
    }
}