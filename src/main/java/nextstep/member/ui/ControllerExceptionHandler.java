package nextstep.member.ui;

import nextstep.member.exception.MemberNotFoundException;
import nextstep.member.exception.PasswordAuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<Void> handleIllegalArgsException(MemberNotFoundException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(PasswordAuthenticationException.class)
    public ResponseEntity<Void> handleIllegalArgsException(PasswordAuthenticationException e) {
        return ResponseEntity.badRequest().build();
    }

}
