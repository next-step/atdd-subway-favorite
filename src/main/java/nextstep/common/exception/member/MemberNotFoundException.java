package nextstep.common.exception.member;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author : Rene Choi
 * @since : 2024/01/31
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MemberNotFoundException extends RuntimeException {
	private HttpStatus status = HttpStatus.BAD_REQUEST;

	public MemberNotFoundException() {
		super();
	}

	public MemberNotFoundException(String message) {
		super(message);
	}

	public MemberNotFoundException(HttpStatus status) {
		super(status.getReasonPhrase());
	}

	public MemberNotFoundException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}
}