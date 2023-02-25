package nextstep;

import nextstep.auth.exception.AuthRestApiException;
import nextstep.member.exception.FavoriteRestApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(AuthRestApiException.class)
    public ResponseEntity<?> handlerException(AuthRestApiException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    
    @ExceptionHandler(FavoriteRestApiException.class)
    public ResponseEntity<?> handlerException(FavoriteRestApiException e) {
        return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body(e.getMessage());
    }
}
