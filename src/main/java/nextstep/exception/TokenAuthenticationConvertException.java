package nextstep.exception;


import nextstep.error.ErrorCode;

public class TokenAuthenticationConvertException extends NextStepException {

    public TokenAuthenticationConvertException() {
        super(ErrorCode.TOKEN_AUTHENTICATION_CONVERT_ERROR);
    }
}
