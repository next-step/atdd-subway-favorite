package nextstep.global.error;

import nextstep.global.error.exception.InvalidValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class SubwayExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> invalidExceptionHandler(InvalidValueException invalidValueException) {
        HttpStatus httpStatus = HttpStatus.valueOf(invalidValueException.getStatus());
        List<String> messages = List.of(invalidValueException.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(httpStatus, messages);
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> runtimeExceptionHandler(RuntimeException runtimeException) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        List<String> messages = List.of(runtimeException.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(httpStatus, messages);
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> illegalArgumentsExceptionHandler(IllegalArgumentException illegalArgumentException) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        if (illegalArgumentException.getMessage() != null) {
            List<String> messages = List.of(illegalArgumentException.getMessage());
            ErrorResponse errorResponse = ErrorResponse.of(httpStatus, messages);
            return ResponseEntity.status(httpStatus).body(errorResponse);
        }
        return ResponseEntity.status(httpStatus).body(ErrorResponse.of(httpStatus, Collections.EMPTY_LIST));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception exception) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        List<String> messages = List.of(exception.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(httpStatus, messages);
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
