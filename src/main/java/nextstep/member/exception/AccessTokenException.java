package nextstep.member.exception;

import nextstep.auth.common.AuthErrorMessage;

public class AccessTokenException extends RuntimeException {

    public AccessTokenException(AuthErrorMessage authErrorMessage) {
        super(authErrorMessage.getMessage());
    }
}
