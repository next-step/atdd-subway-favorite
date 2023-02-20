package nextstep.member.ui;

import nextstep.member.exception.FavoriteNotFoundException;
import nextstep.member.exception.MemberAuthenticationException;
import nextstep.member.exception.MemberNotFoundException;
import nextstep.member.exception.TokenAuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MemberControllerExceptionHandler {

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<Void> handleIllegalArgsException(MemberNotFoundException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MemberAuthenticationException.class)
    public ResponseEntity<Void> handleIllegalArgsException(MemberAuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(TokenAuthorizationException.class)
    public ResponseEntity<Void> handleIllegalArgsException(TokenAuthorizationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(FavoriteNotFoundException.class)
    public ResponseEntity<Void> handleIllegalArgsException(FavoriteNotFoundException e) {
        return ResponseEntity.badRequest().build();
    }

}
