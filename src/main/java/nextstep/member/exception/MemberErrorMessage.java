package nextstep.member.exception;

public enum MemberErrorMessage {
	NOT_FOUND_MEMBER_BY_EMAIL("이메일을 잘못 입력하셨거나, 없는 계정 입니다."),
	NOT_FOUND_MEMBER_BY_PASSWORD("비밀번호를 잘못 입력하셨습니다."),
	UNAUTHENTICATED_TOKEN("유효하지 않은 인증 정보입니다."),
	INVALID_TOKEN("유효하지 않은 토큰입니다."),
	UNAUTHORIZATION_CODE("유효하지 않는 권한입니다");

	private String message;

	MemberErrorMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
