package nextstep.auth;

import lombok.RequiredArgsConstructor;
import nextstep.common.ErrorResponse;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class AuthExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception e) {
        ErrorResponse response = new ErrorResponse(messageSource.getMessage(e.getMessage(), null, Locale.KOREA));
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }
}
