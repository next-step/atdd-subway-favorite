package nextstep;

import nextstep.auth.config.exception.MissingTokenException;
import nextstep.auth.config.exception.ValidateTokenException;
import nextstep.member.config.exception.EmailInputException;
import nextstep.member.config.exception.PasswordMatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingTokenException.class)
    protected ResponseEntity<ErrorResponse> handleMissingTokenException(MissingTokenException e) {
        final ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(ValidateTokenException.class)
    protected ResponseEntity<ErrorResponse> handleValidateTokenException(ValidateTokenException e) {
        final ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(EmailInputException.class)
    protected ResponseEntity<ErrorResponse> handleEmailInputException(EmailInputException e) {
        final ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(PasswordMatchException.class)
    protected ResponseEntity<ErrorResponse> handlePasswordMatchException(PasswordMatchException e) {
        final ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}
