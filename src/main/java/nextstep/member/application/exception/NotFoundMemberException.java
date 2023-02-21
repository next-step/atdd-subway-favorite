package nextstep.member.application.exception;

import nextstep.common.exception.ErrorCode;

public class NotFoundMemberException extends MemberException {

	public NotFoundMemberException(ErrorCode errorCode) {
		super(errorCode);
	}
}
