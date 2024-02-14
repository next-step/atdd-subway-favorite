package nextstep.common.exception.favorite;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author : Rene Choi
 * @since : 2024/01/31
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FavoriteCreationNotValidException extends RuntimeException {
	private HttpStatus status = HttpStatus.BAD_REQUEST;

	public FavoriteCreationNotValidException() {
		super();
	}

	public FavoriteCreationNotValidException(String message) {
		super(message);
	}

	public FavoriteCreationNotValidException(HttpStatus status) {
		super(status.getReasonPhrase());
	}

	public FavoriteCreationNotValidException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}
}