package atdd.user.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class FailedLoginException extends RuntimeException {
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static class InvalidJwtAuthenticationException extends RuntimeException {
        public InvalidJwtAuthenticationException(String message) {
            super(message);
        }
    }
}
