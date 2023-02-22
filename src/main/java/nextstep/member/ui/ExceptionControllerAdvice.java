package nextstep.member.ui;

import nextstep.member.application.exception.UnAuthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity unAuthorizedException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
