package nextstep.subway.unit;

import static nextstep.subway.utils.GithubTestResponses.*;
import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import nextstep.DataLoader;
import nextstep.auth.github.GithubClient;
import nextstep.auth.github.GithubProfileResponse;

@DisplayName("GithubClient 테스트")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {

	@Autowired private DataLoader dataLoader;
	@Autowired private GithubClient githubClient;

	@BeforeEach
	public void setUp() {
		dataLoader.loadData();
	}

	@DisplayName("권한증서(code)로 AccessToken 발급")
	@MethodSource(value = {"githubResponsesCodeAndAccessToken"})
	@ParameterizedTest
	void getAccessTokenFromGithubTest(String code, String accessToken) {
		String accessTokenFromGithub = githubClient.getAccessTokenFromGithub(code);
		assertThat(accessTokenFromGithub).isEqualTo(accessToken);
	}

	private static Stream<Arguments> githubResponsesCodeAndAccessToken() {
		return Stream.of(
			Arguments.of(사용자1.getCode(), 사용자1.getAccessToken()),
			Arguments.of(사용자2.getCode(), 사용자2.getAccessToken()),
			Arguments.of(사용자3.getCode(), 사용자3.getAccessToken()),
			Arguments.of(사용자4.getCode(), 사용자4.getAccessToken())
		);
	}

	@DisplayName("AccessToken으로 Github 프로필 조회 정보 제공")
	@MethodSource(value = {"githubResponsesAccessTokenAndEmail"})
	@ParameterizedTest
	void getGithubProfileFromGithubTest(final String accessToken, final String email) {
		GithubProfileResponse profileFromGithub = githubClient.getGithubProfileFromGithub(accessToken);
		assertThat(profileFromGithub.getEmail()).isEqualTo(email);
	}

	private static Stream<Arguments> githubResponsesAccessTokenAndEmail() {
		return Stream.of(
			Arguments.of(사용자1.getAccessToken(), 사용자1.getEmail()),
			Arguments.of(사용자2.getAccessToken(), 사용자2.getEmail()),
			Arguments.of(사용자3.getAccessToken(), 사용자3.getEmail()),
			Arguments.of(사용자4.getAccessToken(), 사용자4.getEmail())
		);
	}
}
