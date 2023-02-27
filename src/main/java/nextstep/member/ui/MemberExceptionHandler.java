package nextstep.member.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import nextstep.member.exception.InvalidPasswordException;
import nextstep.member.exception.InvalidTokenException;
import nextstep.member.exception.MemberNotFoundException;

@ControllerAdvice
public class MemberExceptionHandler {
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<Void> handleIllegalArgsException(MemberNotFoundException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Void> handleIllegalArgsException(InvalidPasswordException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Void> handleIllegalArgsException(InvalidTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
