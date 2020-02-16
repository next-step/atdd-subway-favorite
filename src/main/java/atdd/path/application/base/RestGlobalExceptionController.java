package atdd.path.application.base;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestGlobalExceptionController {

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public String emptyResultDataAccessException() {
        return "{\"status\": 4040, \"message\": \"조회 결과가 없습니다.\"}";
    }
}
