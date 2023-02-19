package nextstep.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler(value = {NotFoundMemberException.class})
    public ResponseEntity<?> notFoundMemberException(Exception e) {
        log.info("Not Found Exception: {}", e.getMessage(), e);

        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(value = {InvalidGithubTokenException.class})
    public ResponseEntity<?> faildGenerateGithubTokenException(Exception e) {
        log.info("Failed Generate Github Token: {}", e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<?> badRequestException(Exception e) {
        log.info("Bad Request Exception: {}", e.getMessage(), e);

        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> exception(Exception e) {
        log.error("Internal Server Error: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().build();
    }

}
