package nextstep.handler;

import nextstep.authentication.application.exception.AuthenticationException;
import nextstep.favorite.application.exception.NotExistFavoriteException;
import nextstep.line.application.exception.DuplicateStationException;
import nextstep.line.application.exception.LastOneSectionException;
import nextstep.line.application.exception.NotExistLineException;
import nextstep.path.application.exception.NotAddedStationsToPathsException;
import nextstep.path.application.exception.NotConnectedPathsException;
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
            NotConnectedPathsException.class,
            NotAddedStationsToPathsException.class,
            NotExistFavoriteException.class
    })
    public ResponseEntity<String> handleBadRequestException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<String> handleUnauthorizedException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }
}
