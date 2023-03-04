package nextstep.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nextstep.member.exception.InvalidTokenException;
import nextstep.member.exception.NotFoundException;
import nextstep.member.exception.UnAuthenticationException;
import nextstep.member.exception.UnAuthorizationException;

@RestControllerAdvice
public class ControllerExceptionHandler {
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Void> handleIllegalArgsException(DataIntegrityViolationException e) {
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Void> handleIllegalArgsException(IllegalArgumentException e) {
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Void> handleNotFoundException(NotFoundException e) {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(UnAuthenticationException.class)
	public ResponseEntity<Void> handleUnAuthenticationException(UnAuthenticationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<Void> handleInvalidTokenException(InvalidTokenException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@ExceptionHandler(UnAuthorizationException.class)
	public ResponseEntity<Void> handleAuthorizationException(UnAuthorizationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
}
