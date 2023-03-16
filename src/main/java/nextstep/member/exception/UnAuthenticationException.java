package nextstep.member.exception;

public class UnAuthenticationException extends RuntimeException {
	public UnAuthenticationException(MemberErrorMessage message) {
		super(message.getMessage());
	}
}
