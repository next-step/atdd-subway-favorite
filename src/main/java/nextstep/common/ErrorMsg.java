package nextstep.common;

public enum ErrorMsg {
	HEADER_AUTH_IS_NULL("헤더에 인증정보가 포함되어 있지 않습니다."),
	HEADER_AUTH_NOT_BEARER("베어러 인증 정보가 포함되어 있지 않습니다."),
	HEADER_AUTH_INVALID_TOKEN("정상적인 베어러 토큰이 아닙니다."),

	LOGIN_NOT_MATH_EMAIL("해당 이메일로 가입된 회원이 없습니다."),
	LOGIN_NOT_MATCH_PASSWORD("비밀번호가 일치하지 않습니다."),

	GITHUB_NOT_MATCH_CODE("해당 코드와 일치하는 유저 정보가 없습니다."),
	GITHUB_NOT_MATCH_ACCESS_TOKEN("해당 토큰과 일치하는 유저 정보가 없습니다."),
	;

	private final String message;

	ErrorMsg(String message) {
		this.message = message;
	}

	public String isMessage() {
		return message;
	}
}
