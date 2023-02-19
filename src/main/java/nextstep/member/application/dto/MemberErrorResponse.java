package nextstep.member.application.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nextstep.member.application.exception.ErrorCode;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberErrorResponse {

	private String message;

	private String code;

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
