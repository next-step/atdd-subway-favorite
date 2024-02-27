package nextstep.utils.member;

import java.util.Arrays;

public enum GithubAuthFixture {
	사용자1("code1", "accessToken1", "email1@email.com"),
	사용자2("code2", "accessToken2", "email2@email.com"),
	사용자3("code3", "accessToken3", "email3@email.com"),
	사용자4("code4", "accessToken4", "email4@email.com");

	private String code;
	private String accessToken;
	private String email;

	GithubAuthFixture(String code, String accessToken, String email) {
		this.code = code;
		this.accessToken = accessToken;
		this.email = email;
	}

	public String getCode() {
		return code;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getEmail() {
		return email;
	}

	public static String getAccessTokenByCode(String code) {
		return Arrays.stream(values())
				.filter(fixture -> code.equals(fixture.getCode()))
				.findAny()
				.orElseThrow(() -> new IllegalArgumentException("올바른 코드 값이 아닙니다."))
				.getAccessToken();
	}

	public static String getEmailByCode(String accessToken) {
		return Arrays.stream(values())
				.filter(fixture -> accessToken.equals(fixture.getAccessToken()))
				.findAny()
				.orElseThrow(() -> new IllegalArgumentException("올바른 토큰 값이 아닙니다."))
				.getEmail();
	}
}
