package nextstep.auth.exception;

import nextstep.common.exception.UnauthorizedException;

public class AuthenticationException extends UnauthorizedException {
    public AuthenticationException() {
        super("인증되지 않았습니다.");
    }
}
