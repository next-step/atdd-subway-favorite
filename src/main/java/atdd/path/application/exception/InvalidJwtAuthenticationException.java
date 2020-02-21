package atdd.path.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidJwtAuthenticationException extends RuntimeException {
	public InvalidJwtAuthenticationException(final String message) {
		super(message);
	}
}
