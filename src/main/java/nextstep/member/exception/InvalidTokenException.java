package nextstep.member.exception;

public class InvalidTokenException extends RuntimeException {
	public InvalidTokenException(ErrorMessage message) {
		super(message.getMessage());
	}
}
