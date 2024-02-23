package nextstep.core.member.exception;

import nextstep.common.exception.UnauthorizedException;

public class InvalidTokenException extends UnauthorizedException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
