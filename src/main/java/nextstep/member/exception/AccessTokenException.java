package nextstep.member.exception;

import nextstep.member.common.MemberErrorMessage;

public class AccessTokenException extends RuntimeException {

    public AccessTokenException(MemberErrorMessage memberErrorMessage) {
        super(memberErrorMessage.getMessage());
    }
}
