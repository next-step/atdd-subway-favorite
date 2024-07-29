package nextstep.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Map<String, String>> baseException(BaseException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }

}
