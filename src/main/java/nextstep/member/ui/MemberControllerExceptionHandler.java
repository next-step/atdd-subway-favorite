package nextstep.member.ui;

import nextstep.member.domain.MemberException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MemberControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(MemberException.class)
    public ResponseEntity<Object> handleMemberException(Exception e, WebRequest request) {
        return handleExceptionInternal(
                e,
                new ExceptionResponse(e.getMessage()),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    @ExceptionHandler(MemberException.NotFound.class)
    public ResponseEntity<Object> handleMemberNotFoundException(Exception e, WebRequest request) {
        return handleExceptionInternal(
                e,
                new ExceptionResponse(e.getMessage()),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request
        );
    }

    private class ExceptionResponse {
        private final String message;

        public ExceptionResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
