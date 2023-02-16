package nextstep.auth.config.exception;

import nextstep.auth.config.message.AuthError;

public class MissingTokenException extends RuntimeException {

    public MissingTokenException(final AuthError authError) {
        super(authError.getMessage());
    }
}