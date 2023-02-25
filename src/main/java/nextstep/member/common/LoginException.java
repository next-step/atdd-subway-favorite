package nextstep.member.common;

public class LoginException extends BusinessException {
    public LoginException(ErrorResponse errorResponse) {
        super(errorResponse);
    }
}
