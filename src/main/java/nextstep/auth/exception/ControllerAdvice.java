package nextstep.auth.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(AuthRestApiException.class)
    public ResponseEntity<?> handlerException(AuthRestApiException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
