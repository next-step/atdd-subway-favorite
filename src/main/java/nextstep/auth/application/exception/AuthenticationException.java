package nextstep.auth.application.exception;

import nextstep.common.error.exception.UnAuthorizedException;

public class AuthenticationException extends UnAuthorizedException {

    public AuthenticationException() {
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
