package atdd.path.web.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error("illegalargument exception occurred. message=[{}]. className=[{}]", e.getMessage(), e.getClass().getName(), e);
        return ResponseEntity.badRequest().build();
    }
}
