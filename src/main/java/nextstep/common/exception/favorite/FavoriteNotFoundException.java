package nextstep.common.exception.favorite;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author : Rene Choi
 * @since : 2024/01/31
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FavoriteNotFoundException extends RuntimeException {
	private HttpStatus status = HttpStatus.BAD_REQUEST;

	public FavoriteNotFoundException() {
		super();
	}

	public FavoriteNotFoundException(String message) {
		super(message);
	}

	public FavoriteNotFoundException(HttpStatus status) {
		super(status.getReasonPhrase());
	}

	public FavoriteNotFoundException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}
}