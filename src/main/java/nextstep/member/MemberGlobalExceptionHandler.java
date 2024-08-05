package nextstep.member;

import nextstep.common.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberGlobalExceptionHandler {

    @ExceptionHandler(AccessTokenException.class)
    public ResponseEntity<String> lineException(Exception exception) {
        return ErrorResponse.badRequest(exception.getMessage());
    }
}
