package nextstep.auth.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends RuntimeException {

	public static final String NOT_FOUND_EMAIL = "Not found user email";
	public static final String PASSWORD_IS_INCORRECT = "Password is incorrect";

	public AuthenticationException(String message) {
		super(message);
	}
}
