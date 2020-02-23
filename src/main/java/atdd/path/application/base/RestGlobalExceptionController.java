package atdd.path.application.base;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestGlobalExceptionController {

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<String> emptyResultDataAccessException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("조회 결과가 없습니다.");
    }
}
