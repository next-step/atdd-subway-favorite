package nextstep.global;

import nextstep.global.exception.AlreadyRegisteredException;
import nextstep.global.exception.InsufficientStationException;
import nextstep.global.exception.SectionMismatchException;
import nextstep.global.exception.StationNotMatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            SectionMismatchException.class,
            AlreadyRegisteredException.class,
            InsufficientStationException.class,
            StationNotMatchException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(RuntimeException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
