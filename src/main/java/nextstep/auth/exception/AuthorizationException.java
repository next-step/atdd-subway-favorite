package nextstep.auth.exception;

import nextstep.common.exception.GlobalException;

public class AuthorizationException extends GlobalException {
    public AuthorizationException(final String message) {
        super(message);
    }
}
