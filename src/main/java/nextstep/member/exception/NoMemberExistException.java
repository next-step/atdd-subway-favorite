package nextstep.member.exception;

import nextstep.member.common.MemberErrorMessage;

public class NoMemberExistException extends RuntimeException {

    public NoMemberExistException(MemberErrorMessage memberErrorMessage) {
        super(memberErrorMessage.getMessage());
    }
}
