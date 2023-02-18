package nextstep.member.application.exception;

public class InvalidTokenException extends MemberException {

	public InvalidTokenException(ErrorCode errorCode) {
		super(errorCode);
	}
}
