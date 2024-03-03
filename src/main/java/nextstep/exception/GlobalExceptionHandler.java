package nextstep.exception;

import nextstep.auth.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static nextstep.exception.ExceptionMessage.AUTHENTICATION_FAILED;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(BAD_REQUEST).build();
    }

    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<ExceptionResponse> handleApplication(ApplicationException ex) {
        ExceptionResponse response = ExceptionResponse.builder()
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(OK).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        ExceptionResponse response = ExceptionResponse.builder()
                .message(AUTHENTICATION_FAILED.getMessage())
                .build();
        return ResponseEntity.status(UNAUTHORIZED).body(response);
    }


}