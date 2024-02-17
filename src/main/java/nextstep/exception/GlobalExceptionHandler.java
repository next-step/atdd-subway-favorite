package nextstep.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException: {}", ex);
        return ResponseEntity.status(BAD_REQUEST).build();
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        log.error("AuthenticationException: {}", ex);
        ExceptionResponse response = ExceptionResponse.builder()
                .message("인증에 실패했습니다.")
                .build();
        return ResponseEntity.status(UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<ExceptionResponse> handleApplication(ApplicationException ex) {
        log.error("ApplicationException: {}", ex);
        ExceptionResponse response = ExceptionResponse.builder()
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(OK).body(response);
    }

}
