package nextstep.member.application.dto;

import nextstep.member.application.exception.ErrorCode;

public class MemberErrorResponse {

	private final String message;

	private final String code;

	private MemberErrorResponse(String message, String code) {
		this.message = message;
		this.code = code;
	}

	public static MemberErrorResponse of(ErrorCode errorCode) {
		return new MemberErrorResponse(errorCode.getMessage(), errorCode.getCode());
	}

	public String getMessage() {
		return message;
	}

	public String getCode() {
		return code;
	}
}
