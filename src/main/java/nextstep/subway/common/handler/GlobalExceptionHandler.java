package nextstep.subway.common.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import nextstep.subway.auth.exception.NotValidPasswordException;
import nextstep.subway.auth.exception.UnauthorizedException;
import nextstep.subway.common.exception.BusinessException;
import nextstep.subway.member.exception.NotExistEmailException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = {
		NotValidPasswordException.class, NotExistEmailException.class,
		UnauthorizedException.class
	})
	public ResponseEntity<String> handleUnAuthorizedException(BusinessException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<String> handleBusinessException(BusinessException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}
