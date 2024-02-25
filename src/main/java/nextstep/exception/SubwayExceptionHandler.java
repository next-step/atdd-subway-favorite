package nextstep.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class SubwayExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException e) {
		return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException e) {
		return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(e.getMessage()));
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return ResponseEntity.internalServerError().body(new ExceptionResponse("예상치 못한 문제가 발생했습니다."));
	}
}
