package nextstep.auth;

import nextstep.common.ErrorResponse;
import nextstep.member.exception.AccessTokenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthGlobalExceptionHandler {

    @ExceptionHandler(AccessTokenException.class)
    public ResponseEntity<String> invalidTokenException(Exception exception) {
        return ErrorResponse.badRequest(exception.getMessage());
    }
}
