package nextstep.support.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ErrorResponse> handleApiException(ApiException e) {
    logger.warn("ApiException : {}", e.getMessage(), e);
    return new ResponseEntity<>(
        ErrorResponse.of(e.getErrorCode(), e.getData()), e.getErrorCode().getStatus());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    logger.error("Exception : {}", e.getMessage(), e);
    return new ResponseEntity<>(
        ErrorResponse.from(ErrorCode.DEFAULT), ErrorCode.DEFAULT.getStatus());
  }
}
