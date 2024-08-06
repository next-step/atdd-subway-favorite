package nextstep.member;

import nextstep.exceptions.BaseException;
import nextstep.exceptions.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends BaseException {

    public AuthenticationException() {
        super(ErrorMessage.UNAUTHORIZED);
    }
}
