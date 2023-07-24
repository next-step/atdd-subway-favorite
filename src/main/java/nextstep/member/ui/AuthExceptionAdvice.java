package nextstep.member.ui;

import io.jsonwebtoken.MalformedJwtException;
import nextstep.member.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AuthExceptionAdvice {
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler({MalformedJwtException.class})
  public ResponseEntity<ExceptionResponse> handleWrongJwtException(Exception exception){
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.from(exception));
  }

}
