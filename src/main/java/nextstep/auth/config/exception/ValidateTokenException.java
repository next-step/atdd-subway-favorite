package nextstep.auth.config.exception;

import nextstep.auth.config.message.AuthError;

public class ValidateTokenException extends RuntimeException {

    public ValidateTokenException(final AuthError authError) {
        super(authError.getMessage());
    }
}