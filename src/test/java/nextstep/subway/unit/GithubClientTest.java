package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import nextstep.member.application.GithubClient;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.exception.MemberErrorMessage;
import nextstep.member.exception.UnAuthorizationException;
import nextstep.subway.utils.GithubResponses;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {

	@Autowired
	private GithubClient githubClient;

	public static Stream<Arguments> providerOfGithubUserCodeAndAccessToken() {
		return Stream.of(
			Arguments.of(GithubResponses.사용자1.getCode(), GithubResponses.사용자1.getAccessToken()),
			Arguments.of(GithubResponses.사용자2.getCode(), GithubResponses.사용자2.getAccessToken()),
			Arguments.of(GithubResponses.사용자3.getCode(), GithubResponses.사용자3.getAccessToken()),
			Arguments.of(GithubResponses.사용자4.getCode(), GithubResponses.사용자4.getAccessToken())
		);
	}

	public static Stream<Arguments> providerOfGithubAccessTokenAndProfile() {
		return Stream.of(
			Arguments.of(GithubResponses.사용자1.getAccessToken(), GithubResponses.사용자1.getEmail()),
			Arguments.of(GithubResponses.사용자2.getAccessToken(), GithubResponses.사용자2.getEmail()),
			Arguments.of(GithubResponses.사용자3.getAccessToken(), GithubResponses.사용자3.getEmail()),
			Arguments.of(GithubResponses.사용자4.getAccessToken(), GithubResponses.사용자4.getEmail())
		);
	}

	@DisplayName("유효한 권한증서를 통해 accessToken 을 획득한다.")
	@ParameterizedTest
	@MethodSource(value = "providerOfGithubUserCodeAndAccessToken")
	void getAccessTokenFromGithub(String codeOfGithubUser, String accessToken) {
		// When
		GithubAccessTokenResponse response = githubClient.getAccessTokenFromGithub(codeOfGithubUser);

		// Then
		assertThat(response.getAccessToken()).isEqualTo(accessToken);
	}

	@DisplayName("github의 accessToken 이 null 이면 예외를 던진다.")
	@Test
	void getAccessTokenFromGithub_fail_IF_GITHUB_ACCESS_TOKEN_IS_NULL() {
		// When & Then
		assertThatThrownBy(
			() -> githubClient.getAccessTokenFromGithub(GithubResponses.ACCESS_TOKEN_없는_사용자5.getCode()))
			.isInstanceOf(UnAuthorizationException.class)
			.hasMessage(MemberErrorMessage.UNAUTHORIZATION_CODE.getMessage());
	}

	@DisplayName("유효한 accessToken을 통해 github의 profile을 획득한다.")
	@ParameterizedTest
	@MethodSource(value = "providerOfGithubAccessTokenAndProfile")
	void getGithubProfileFromGithub(String accessToken, String profile) {
		// When
		GithubProfileResponse response = githubClient.getGithubProfileFromGithub(accessToken);

		// Then
		assertThat(response.getEmail()).isEqualTo(profile);
	}

	@DisplayName("github의 profile이 없으면 예외를 던진다.")
	@Test
	void getGithubProfileFromGithub_fail_IF_GITHUB_ACCESS_TOKEN_IS_NULL() {
		// When & Then
		assertThatThrownBy(
			() -> githubClient.getGithubProfileFromGithub(GithubResponses.PROFILE_없는_사용자6.getAccessToken()))
			.isInstanceOf(UnAuthorizationException.class)
			.hasMessage(MemberErrorMessage.UNAUTHORIZATION_CODE.getMessage());
	}
}
