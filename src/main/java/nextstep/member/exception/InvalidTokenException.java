package nextstep.member.exception;

public class InvalidTokenException extends RuntimeException {
	public InvalidTokenException(MemberErrorMessage message) {
		super(message.getMessage());
	}
}
