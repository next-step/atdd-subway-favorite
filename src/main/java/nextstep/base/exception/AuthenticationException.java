package nextstep.base.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends BaseDomainException {
    public AuthenticationException() {
        super(HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED", "unauthorized");
    }
}
