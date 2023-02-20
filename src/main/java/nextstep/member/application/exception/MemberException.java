package nextstep.member.application.exception;

import nextstep.common.exception.ErrorCode;

public class MemberException extends RuntimeException {

	private final ErrorCode errorCode;

	public MemberException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
