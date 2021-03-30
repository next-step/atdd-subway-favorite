package nextstep.subway;

import nextstep.subway.auth.exception.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity handleInvalidSectionException(AuthenticationException e) {
    return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
  }
}
