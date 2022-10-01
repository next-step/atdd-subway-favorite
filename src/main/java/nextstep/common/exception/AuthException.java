package nextstep.common.exception;

import nextstep.common.exception.code.AuthCode;

public class AuthException extends CustomException{
    public AuthException(final AuthCode authCode) {
        super(authCode);
    }
}
