package nextstep.subway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({NoSuchLineException.class, NoSuchStationException.class})
    public ResponseEntity<Void> handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({IllegalSectionException.class, IllegalPathException.class})
    public ResponseEntity<Void> handleBadRequestException() {
        return ResponseEntity.badRequest().build();
    }
}
