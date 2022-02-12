package nextstep.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import nextstep.common.domain.exception.ErrorMessage;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotLoggedInException extends RuntimeException {
    public NotLoggedInException() {
        super(ErrorMessage.NOT_LOGGED_IN_EXCEPTION.getMessage());
    }
}
