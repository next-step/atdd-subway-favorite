package subway.fixture.member;

import java.util.Arrays;
import java.util.NoSuchElementException;

import subway.dto.member.GithubAccessTokenResponse;
import subway.dto.member.GithubProfileResponse;

public enum GithubResponses {
	사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com", 21),
	사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com", 22),
	사용자3("afnm93fmdodf", "access_token_3", "email3@email.com", 23),
	사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com", 24);

	private String code;
	private String accessToken;
	private String email;
	private Integer age;

	GithubResponses(String code, String accessToken, String email, Integer age) {
		this.code = code;
		this.accessToken = accessToken;
		this.email = email;
		this.age = age;
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

	public Integer getAge() {
		return age;
	}

	public static GithubAccessTokenResponse findAccessTokenByCode(String code) {
		return Arrays.stream(values())
			.filter(githubResponses -> githubResponses.code.equals(code))
			.findFirst()
			.map(githubResponses -> new GithubAccessTokenResponse(githubResponses.getAccessToken()))
			.orElseThrow(() -> new NoSuchElementException("code에 맞는 유저가 없습니다."));
	}

	public static GithubProfileResponse findProfileByAccessToken(String accessToken) {
		return Arrays.stream(values())
			.filter(githubResponses -> githubResponses.accessToken.equals(accessToken))
			.findFirst()
			.map(githubResponses -> new GithubProfileResponse(githubResponses.getEmail(), githubResponses.getAge()))
			.orElseThrow(() -> new NoSuchElementException("code에 맞는 유저가 없습니다."));
	}
}
