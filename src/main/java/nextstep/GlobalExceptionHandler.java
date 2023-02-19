package nextstep;

import nextstep.auth.config.exception.MissingTokenException;
import nextstep.auth.config.exception.ValidateTokenException;
import nextstep.member.config.exception.EmailInputException;
import nextstep.member.config.exception.PasswordMatchException;
import nextstep.subway.config.exception.NotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingTokenException.class)
    protected ResponseEntity<ErrorResponse> handleMissingTokenException(MissingTokenException e) {
        final ErrorResponse errorResponse = ErrorResponse.from(e.getMessage());
        return ResponseEntity.status(UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(ValidateTokenException.class)
    protected ResponseEntity<ErrorResponse> handleValidateTokenException(ValidateTokenException e) {
        final ErrorResponse errorResponse = ErrorResponse.from(e.getMessage());
        return ResponseEntity.status(UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(EmailInputException.class)
    protected ResponseEntity<ErrorResponse> handleEmailInputException(EmailInputException e) {
        final ErrorResponse errorResponse = ErrorResponse.from(e.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(PasswordMatchException.class)
    protected ResponseEntity<ErrorResponse> handlePasswordMatchException(PasswordMatchException e) {
        final ErrorResponse errorResponse = ErrorResponse.from(e.getMessage());
        return ResponseEntity.status(UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Void> handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgsException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        final ErrorResponse errorResponse = ErrorResponse.from(e.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        final List<String> errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        final ErrorResponse errorResponse = new ErrorResponse(errors);
        return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
    }
}
