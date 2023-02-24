package nextstep.member.exception;

public enum ErrorMessage {
	NOT_FOUND_MEMBER_BY_EMAIL("이메일을 잘못 입력하셨거나, 없는 계정 입니다."),
	NOT_FOUND_MEMBER_BY_PASSWORD("비밀번호를 잘못 입력하셨습니다.");

	private String message;

	ErrorMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
