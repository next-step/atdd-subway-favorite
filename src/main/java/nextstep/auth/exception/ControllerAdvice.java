package nextstep.auth.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(AuthRestApiException.class)
    public ResponseEntity<?> handlerException(AuthRestApiException e) {
        return ResponseEntity.status(e.getErrorResponse().getHttpStatus())
                .body(new ErrorResponse(e.getErrorResponse().getMessage()));
    }
}
