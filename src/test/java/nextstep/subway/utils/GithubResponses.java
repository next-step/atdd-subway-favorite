package nextstep.subway.utils;

import java.util.Arrays;

import nextstep.member.exception.NotFoundException;

public enum GithubResponses {
	사용자1("code1", "access_token_1", "email1@email.com"),
	사용자2("code2", "access_token_2", "email2@email.com"),
	사용자3("code3", "access_token_3", "email3@email.com"),
	사용자4("code4", "access_token_4", "email4@email.com");

	private final String code;
	private final String accessToken;
	private final String email;

	GithubResponses(String code, String accessToken, String email) {
		this.code = code;
		this.accessToken = accessToken;
		this.email = email;
	}

	public static String getAccessTokenFromCode(final String code) {
		return Arrays.stream(GithubResponses.values())
			.filter(user -> user.code.equals(code))
			.findFirst()
			.map(GithubResponses::getAccessToken)
			.orElseThrow(() -> {
				throw new NotFoundException();
			});
	}

	public static String getEmailFromAccessToken(final String accessToken) {
		return Arrays.stream(GithubResponses.values())
			.filter(user -> user.accessToken.equals(accessToken))
			.findFirst()
			.map(GithubResponses::getEmail)
			.orElseThrow(() -> {
				throw new NotFoundException();
			});
	}

	public String getCode() {
		return code;
	}

	public String getEmail() {
		return email;
	}

	public String getAccessToken() {
		return accessToken;
	}
}
