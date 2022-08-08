package nextstep.subway.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ExceptionResponse> customException(CustomException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ExceptionResponse.getInstance(e.getMessage()));
  }
}
