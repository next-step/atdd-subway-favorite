package nextstep.subway.utils;

import static nextstep.common.ErrorMsg.*;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GithubTestResponses {
	사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
	사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
	사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
	사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com");

	private String code;
	private String accessToken;
	private String email;

	public static GithubTestResponses findByCode(String code) {
		return Arrays.stream(values())
			.filter(user -> user.code.equals(code))
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException(GITHUB_NOT_MATCH_CODE.isMessage()));
	}
	public static GithubTestResponses findByToken(String accessToken) {
		return Arrays.stream(values())
			.filter(user -> user.accessToken.equals(accessToken))
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException(GITHUB_NOT_MATCH_ACCESS_TOKEN.isMessage()));
	}
}
