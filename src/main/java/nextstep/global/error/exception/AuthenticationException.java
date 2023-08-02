package nextstep.global.error.exception;

import nextstep.global.error.code.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends BusinessException {

    public AuthenticationException(ErrorCode errorCode) {
        super(HttpStatus.UNAUTHORIZED.value(), errorCode.getMessage());
    }

}
