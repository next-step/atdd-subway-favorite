package nextstep.common.exception;

public class LoginException extends BusinessException {
    public LoginException(ErrorResponse errorResponse) {
        super(errorResponse);
    }
}
