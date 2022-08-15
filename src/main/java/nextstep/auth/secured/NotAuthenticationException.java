package nextstep.auth.secured;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotAuthenticationException extends RuntimeException {

    public NotAuthenticationException(String message) {
        super(message);
    }
}
