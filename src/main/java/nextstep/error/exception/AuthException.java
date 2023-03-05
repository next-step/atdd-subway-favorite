package nextstep.error.exception;

public class AuthException extends BusinessException {

	public AuthException(ErrorCode errorCode) {
		super(errorCode);
	}
}