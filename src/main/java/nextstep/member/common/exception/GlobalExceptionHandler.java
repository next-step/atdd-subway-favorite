package nextstep.member.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> handleIllegalArgumentException(final LoginException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
