package nextstep.subway.common.advice;

import nextstep.subway.auth.exception.AuthenticationFailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SubwayControllerAdvice {

    @ExceptionHandler(AuthenticationFailException.class)
    public ResponseEntity<Void> handleAuthException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
