package nextstep.member.application.exception;

public enum MemberErrorCode implements ErrorCode {
	NOT_FOUND_MEMBER("존재하지 않은 회원입니다."),
	INVALID_TOKEN("만료되거나, 잘못된 토큰입니다.");

	private final String message;

	MemberErrorCode(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public String getCode() {
		return this.name();
	}
}
