package nextstep.subway.common.handler;

import nextstep.subway.auth.exception.InvalidPasswordException;
import nextstep.subway.auth.exception.NotExistEmailException;
import nextstep.subway.common.exception.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Void> handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> badRequestExceptionHandler(BadRequestException e) {
        return ResponseEntity.badRequest().body(ExceptionResponse.of(e.getMessage()));
    }

    @ExceptionHandler(value = {NotExistEmailException.class, InvalidPasswordException.class})
    public ResponseEntity<ExceptionResponse> authExceptionHandler(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.of(e.getMessage()));
    }
}
