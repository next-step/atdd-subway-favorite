package nextstep.member;

import nextstep.exceptions.BaseException;
import nextstep.exceptions.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AuthorizationException extends BaseException {

    public AuthorizationException() {
        super(ErrorMessage.FORBIDDEN);
    }
}
