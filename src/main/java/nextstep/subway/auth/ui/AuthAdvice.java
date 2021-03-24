package nextstep.subway.auth.ui;

import nextstep.subway.auth.exception.InvalidAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthAdvice {
    @ExceptionHandler({ InvalidAuthenticationException.class })
    public ResponseEntity<String> handleInvalidAuthenticationException(
        InvalidAuthenticationException e
    ) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
