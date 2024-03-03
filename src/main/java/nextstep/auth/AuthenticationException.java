package nextstep.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "UNAUTHORIZED")
public class AuthenticationException extends RuntimeException {
    private String message;

    public AuthenticationException() {

    }

    public AuthenticationException(String message) {
        this.message = message;
    }
}
