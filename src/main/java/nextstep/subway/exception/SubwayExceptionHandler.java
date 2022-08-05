package nextstep.subway.exception;

import nextstep.common.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SubwayExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> notFoundExceptionHandler(NotFoundStationException e) {
        ErrorResponse response = ErrorResponse.of(HttpStatus.NOT_FOUND, e);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
