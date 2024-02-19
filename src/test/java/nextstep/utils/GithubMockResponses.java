package nextstep.utils;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/02/17
 */

@Getter
@RequiredArgsConstructor
public enum GithubMockResponses {
	사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com",1L, "user1"),
	사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com", 2L,"user2"),
	사용자3("afnm93fmdodf", "access_token_3", "email3@email.com", 3L,"user3"),
	사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com",4L,"user4");

	private final String code;
	private final String accessToken;
	private final String email;
	private final Long id;
	private final String login;


	public static String fetchAccessTokenByCode(String code) {
		return Arrays.stream(GithubMockResponses.values())
			.filter(response -> response.isCodeSame(code))
			.findFirst()
			.map(GithubMockResponses::getAccessToken)
			.orElse(null);
	}
	public static String fetchEmailByAccessToken(String accessToken) {
		return Arrays.stream(GithubMockResponses.values())
			.filter(response -> response.isAccessTokenSame(accessToken))
			.findFirst()
			.map(GithubMockResponses::getEmail)
			.orElse(null);
	}

	public static Long fetchIdByAccessToken(String accessToken) {
		return Arrays.stream(GithubMockResponses.values())
			.filter(response -> response.isAccessTokenSame(accessToken))
			.findFirst()
			.map(GithubMockResponses::getId)
			.orElse(null);
	}

	public static String fetchLoginByAccessToken(String accessToken) {
		return Arrays.stream(GithubMockResponses.values())
			.filter(response -> response.isAccessTokenSame(accessToken))
			.findFirst()
			.map(GithubMockResponses::getLogin)
			.orElse(null);
	}



	private boolean isAccessTokenSame(String otherToken) {
		return this.accessToken.equals(otherToken);
	}
	private boolean isCodeSame(String otherCode) {
		return this.code.equals(otherCode);
	}
}