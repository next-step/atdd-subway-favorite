package nextstep.handler;

import nextstep.line.application.exception.DuplicateStationException;
import nextstep.line.application.exception.LastOneSectionException;
import nextstep.line.application.exception.NotExistLineException;
import nextstep.path.application.exception.NotAddedStationsToSectionException;
import nextstep.path.application.exception.NotConnectedStationsException;
import nextstep.path.ui.exception.SameSourceAndTargetException;
import nextstep.station.application.exception.NotExistStationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NotExistStationException.class, NotExistLineException.class})
    public ResponseEntity<String> handleNotFoundException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler({
            LastOneSectionException.class,
            DuplicateStationException.class,
            SameSourceAndTargetException.class,
            NotConnectedStationsException.class,
            NotAddedStationsToSectionException.class
    })
    public ResponseEntity<String> handleBadRequestException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}
