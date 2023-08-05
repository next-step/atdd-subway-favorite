package nextstep.exception;

import static nextstep.exception.SubwayError.AUTHENTICATION_EXCEPTION;

public class AuthenticationException extends SubwayException {
    public AuthenticationException() {
        super(AUTHENTICATION_EXCEPTION);
    }
}
