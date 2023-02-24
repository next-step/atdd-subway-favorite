package nextstep.member.exception;

public class UnAuthenticationException extends RuntimeException {
	public UnAuthenticationException(ErrorMessage message) {
		super(message.getMessage());
	}
}
