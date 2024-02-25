package nextstep.core.member.exception;

import nextstep.common.exception.UnauthorizedException;

public class NonMatchingMemberException extends UnauthorizedException {
    public NonMatchingMemberException(String message) {
        super(message);
    }
}
