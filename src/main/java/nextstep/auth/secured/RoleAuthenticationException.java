package nextstep.auth.secured;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class RoleAuthenticationException extends RuntimeException {

    public RoleAuthenticationException() {
    }

    public RoleAuthenticationException(String message) {
        super(message);
    }
}
