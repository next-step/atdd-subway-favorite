package nextstep.member.exception;

public class UnAuthorizationException extends RuntimeException {
	private static final MemberErrorMessage DEFAULT_MESSAGE = MemberErrorMessage.UNAUTHORIZATION_CODE;

	public UnAuthorizationException() {
		super(DEFAULT_MESSAGE.getMessage());
	}
}
