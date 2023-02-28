package nextstep.common.exception;

import nextstep.auth.exception.AuthorizationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> authorizationException(AuthorizationException e) {
        final var message = e.getMessage();
        return ResponseEntity.status(UNAUTHORIZED).body(new ErrorResponse(message));
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResponse> globalException(GlobalException e) {
        final var message = e.getMessage();
        return ResponseEntity.badRequest().body(new ErrorResponse(message));
    }
}
