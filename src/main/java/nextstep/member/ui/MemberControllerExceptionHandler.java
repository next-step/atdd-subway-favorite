package nextstep.member.ui;

import nextstep.member.exception.MemberNotFoundException;
import nextstep.member.exception.PasswordAuthenticationException;
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

    @ExceptionHandler(PasswordAuthenticationException.class)
    public ResponseEntity<Void> handleIllegalArgsException(PasswordAuthenticationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(TokenAuthorizationException.class)
    public ResponseEntity<Void> handleIllegalArgsException(TokenAuthorizationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
