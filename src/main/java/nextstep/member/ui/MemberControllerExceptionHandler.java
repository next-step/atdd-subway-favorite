package nextstep.member.ui;

import nextstep.member.application.MemberNotFoundException;
import nextstep.member.application.WrongPasswordException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MemberControllerExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Void> handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<Void> handleIllegalArgsException(MemberNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<Void> handleIllegalArgsException(WrongPasswordException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
