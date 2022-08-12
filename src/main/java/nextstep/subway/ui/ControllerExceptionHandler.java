package nextstep.subway.ui;

import nextstep.subway.applicaion.favorite.exception.FavoriteException;
import nextstep.subway.applicaion.favorite.exception.InvalidFavoriteOwnerException;
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

    @ExceptionHandler(InvalidFavoriteOwnerException.class)
    public ResponseEntity<Void> handleInvalidFavoriteOwnerException(InvalidFavoriteOwnerException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(FavoriteException.class)
    public ResponseEntity<Void> handleFavoriteException(FavoriteException e) {
        return ResponseEntity.badRequest().build();
    }

}
