package nextstep.subway.ui;

import nextstep.favorite.exception.CannotDeleteNotMineFavoriteException;
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

    @ExceptionHandler(CannotDeleteNotMineFavoriteException.class)
    public ResponseEntity<Void> handleICannotDeleteNotMineFavoriteException(CannotDeleteNotMineFavoriteException e) {
        return ResponseEntity.badRequest().build();
    }
}
