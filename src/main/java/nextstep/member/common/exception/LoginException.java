package nextstep.member.common.exception;

import nextstep.member.common.exception.BusinessException;
import nextstep.member.common.exception.ErrorResponse;

public class LoginException extends BusinessException {
    public LoginException(ErrorResponse errorResponse) {
        super(errorResponse);
    }
}
