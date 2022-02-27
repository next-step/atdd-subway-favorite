package nextstep.global.error;

import nextstep.global.error.exception.ErrorCode;
import nextstep.global.error.exception.NotFoundMemberException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundMemberException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundMemberException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.NOT_FOUND_MEMBER);
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
