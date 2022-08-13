package nextstep.member.ui;

import nextstep.auth.secured.RoleAuthenticationException;
import nextstep.member.domain.MemberErrorMessage;
import nextstep.common.exception.ErrorResponse;
import nextstep.member.ui.exception.FavoriteOwnerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberGlobalControllerExceptionHandler {

    @ExceptionHandler(FavoriteOwnerException.class)
    public ResponseEntity<ErrorResponse> favoriteOwnerException(FavoriteOwnerException e) {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(RoleAuthenticationException.class)
    public ResponseEntity<ErrorResponse> roleAuthenticationException(RoleAuthenticationException e) {
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.UNAUTHORIZED.value(),
                MemberErrorMessage.UNAUTHORIZED.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}
