package nextstep.member.application.exception;

public class NotFoundMemberException extends MemberException {

	public NotFoundMemberException(ErrorCode errorCode) {
		super(errorCode);
	}
}
