package nextstep.member.config.exception;

import nextstep.member.config.message.MemberError;

public class EmailInputException extends RuntimeException {

    public EmailInputException(final MemberError memberError) {
        super(memberError.getMessage());
    }
}