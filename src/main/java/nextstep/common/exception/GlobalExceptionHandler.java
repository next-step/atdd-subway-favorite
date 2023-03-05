package nextstep.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthorizationException.class)
    protected ResponseEntity<ErrorResponse> handleAuthorizationException(AuthorizationException e) {
        return getErrorResponseResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        return getErrorResponseResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        return getErrorResponseResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(NoPathException.class)
    protected ResponseEntity<ErrorResponse> handleNoPathException(NoPathException e) {
        return getErrorResponseResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(CanNotDeleteFavoriteException.class)
    protected ResponseEntity<ErrorResponse> handleCanNotDeleteFavoriteException(CanNotDeleteFavoriteException e) {
        return getErrorResponseResponseEntity(e.getErrorCode());
    }

    private ResponseEntity<ErrorResponse> getErrorResponseResponseEntity(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.name(), errorCode.getMessage());
    }
}