package nextstep.subway.common.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import nextstep.subway.common.exception.BusinessException;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<String> handleBusinessException(BusinessException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}
