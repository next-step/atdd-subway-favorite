package nextstep.member.exception;

public class UnAuthorizationException extends RuntimeException {
	private static final ErrorMessage DEFAULT_MESSAGE = ErrorMessage.UNAUTHORIZATION_CODE;

	public UnAuthorizationException() {
		super(DEFAULT_MESSAGE.getMessage());
	}
}
