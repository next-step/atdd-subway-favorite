package nextstep.subway.favorite.ui;

import nextstep.subway.error.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FavoriteExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleNameArgsException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleIllegalArgsException(NotFoundException e) {
        return ResponseEntity.badRequest().build();
    }
}
