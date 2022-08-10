package nextstep.subway.ui;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.secured.RoleAuthenticationException;
import nextstep.favorite.exception.NotOwnerFavoriteException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Void> handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgsException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(RoleAuthenticationException.class)
    public ResponseEntity<Void> handleRoleAuthenticationException(RoleAuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(NotOwnerFavoriteException.class)
    public ResponseEntity<Void> handleNotOwnerAuthenticationException(NotOwnerFavoriteException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
