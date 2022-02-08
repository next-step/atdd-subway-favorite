package nextstep.subway.common.handler;

import nextstep.subway.auth.exception.AuthorizeException;
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> exceptionHandler(Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().body(ExceptionResponse.of(e.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Void> handleIllegalArgsException(DataIntegrityViolationException e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> badRequestExceptionHandler(BadRequestException e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().body(ExceptionResponse.of(e.getMessage()));
    }

    @ExceptionHandler({AuthorizeException.class, NotExistEmailException.class, InvalidPasswordException.class})
    public ResponseEntity<ExceptionResponse> authorizeExceptionHandler(RuntimeException e) {
        return new ResponseEntity<>(ExceptionResponse.of(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}
