package nextstep.common;

public enum ErrorMsg {
	LOGIN_NOT_FIND_MEMBER("해당 이메일로 가입된 회원이 없습니다."),
	LOGIN_NOT_MATCH_PASSWORD("비밀번호가 일치하지 않습니다.")
	;

	private final String message;

	ErrorMsg(String message) {
		this.message = message;
	}

	public String isMessage() {
		return message;
	}
}
