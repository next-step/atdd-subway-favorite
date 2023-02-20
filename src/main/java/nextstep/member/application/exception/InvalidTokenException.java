package nextstep.member.application.exception;

import nextstep.common.exception.ErrorCode;

public class InvalidTokenException extends MemberException {

	public InvalidTokenException(ErrorCode errorCode) {
		super(errorCode);
	}
}
