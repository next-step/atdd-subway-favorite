package nextstep.subway;

import nextstep.common.ErrorResponse;
import nextstep.subway.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SubwayGlobalExceptionHandler {

    @ExceptionHandler(NoLineExistException.class)
    public ResponseEntity<String> lineException(Exception exception) {
        return ErrorResponse.badRequest(exception.getMessage());
    }

    @ExceptionHandler({NoStationException.class, NotSameUpAndDownStationException.class, AlreadyHasUpAndDownStationException.class})
    public ResponseEntity<String> stationException(Exception exception) {
        return ErrorResponse.badRequest(exception.getMessage());
    }

    @ExceptionHandler(CannotDeleteSectionException.class)
    public ResponseEntity<String> sectionException(Exception exception) {
        return ErrorResponse.badRequest(exception.getMessage());
    }

    @ExceptionHandler(NotConnectedStationException.class)
    public ResponseEntity<String> pathException(Exception exception) {
        return ErrorResponse.badRequest(exception.getMessage());
    }
}
