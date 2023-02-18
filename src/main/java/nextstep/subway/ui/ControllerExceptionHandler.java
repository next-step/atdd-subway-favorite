package nextstep.subway.ui;

import nextstep.exception.ErrorDTO;
import nextstep.exception.SubwayException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Void> handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgsException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ErrorDTO> handleRuntimeException(SubwayException exception) {
        return ResponseEntity.status(exception.getErrorMessage().getHttpStatus())
                .body(new ErrorDTO(exception.getErrorMessage()));
    }
}
