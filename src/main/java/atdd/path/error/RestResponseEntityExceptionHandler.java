package atdd.path.error;

import atdd.path.application.exception.InvalidJwtAuthenticationException;
import atdd.path.exception.MemberNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	public RestResponseEntityExceptionHandler() {
		super();
	}

	@ExceptionHandler(value = { EmptyResultDataAccessException.class })
	public ResponseEntity<Object> handleEmptyResultDataAccessException() {
		final String bodyOfResponse = "데이터를 찾을 수 없습니다.";
		return new ResponseEntity<>(bodyOfResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = { MemberNotFoundException.class })
	public ResponseEntity<Object> handleIllegalArgumentException() {
		final String bodyOfResponse = "해당 member가 존재하지 않습니다.";
		return new ResponseEntity<>(bodyOfResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = {InvalidJwtAuthenticationException.class})
	public ResponseEntity<Object> handleInvalidJwtAuthenticationException(InvalidJwtAuthenticationException e) {
		final String bodyOfResponse = e.getLocalizedMessage();
		return new ResponseEntity<>(bodyOfResponse, HttpStatus.UNAUTHORIZED);
	}
}
