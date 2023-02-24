package nextstep.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nextstep.member.exception.NotFoundException;

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

}
