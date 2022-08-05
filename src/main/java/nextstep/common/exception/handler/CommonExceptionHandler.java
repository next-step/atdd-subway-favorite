package nextstep.common.exception.handler;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.secured.RoleAuthenticationException;
import nextstep.common.exception.code.CommonErrorCode;
import nextstep.common.exception.dto.ErrorResponse;
import nextstep.common.exception.exception.EntityNotFoundException;
import nextstep.common.exception.utils.ExceptionHandlerUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Void> handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgsException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        CommonErrorCode errorCode = CommonErrorCode.ENTITY_NOT_FOUND;
        return ExceptionHandlerUtils.buildErrorResponse(errorCode, e);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        CommonErrorCode errorCode = CommonErrorCode.UNAUTHORIZED;
        return ExceptionHandlerUtils.buildErrorResponse(errorCode, e);
    }

    @ExceptionHandler(RoleAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleRoleAuthenticationException(RoleAuthenticationException e) {
        CommonErrorCode errorCode = CommonErrorCode.FORBIDDEN;
        return ExceptionHandlerUtils.buildErrorResponse(errorCode, e);
    }
}
