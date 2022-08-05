package nextstep.subway.ui;

import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import nextstep.subway.exception.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> notFoundException(final NotFoundException e, final HttpServletRequest request) {
        return ResponseEntity.badRequest().body(
            ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), e.getMessage(),
                LocalDateTime.now(), request.getRequestURL().toString(), HttpStatus.BAD_REQUEST.getReasonPhrase())
        );
    }

    private static class ErrorResponse {
        private final int status;
        private final String message;
        private final LocalDateTime timeStamp;
        private final String path;
        private final String error;

        private ErrorResponse(final int status, final String message, final LocalDateTime timeStamp, final String path, final String error) {
            this.status = status;
            this.message = message;
            this.timeStamp = timeStamp;
            this.path = path;
            this.error = error;
        }

        public static ErrorResponse of(final int status, final String message, final LocalDateTime timeStamp, final String path, final String error) {
            return new ErrorResponse(status, message, timeStamp, path, error);
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public LocalDateTime getTimeStamp() {
            return timeStamp;
        }

        public String getPath() {
            return path;
        }

        public String getError() {
            return error;
        }

    }
}
