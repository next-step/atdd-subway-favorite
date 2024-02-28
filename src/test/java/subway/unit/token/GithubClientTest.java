package subway.unit.token;

import static org.assertj.core.api.Assertions.*;

import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import subway.acceptance.AcceptanceTest;
import subway.dto.member.GithubProfileResponse;
import subway.fixture.member.GithubResponses;
import subway.member.GithubClient;
import subway.member.GithubClientProperties;
import subway.member.GithubUrlProperties;

class GithubClientTest extends AcceptanceTest {
	private GithubClient githubClient;

	@BeforeEach
	public void setUp(@Autowired ApplicationContext context) {
		Environment env = context.getEnvironment();
		int port = Integer.parseInt(Objects.requireNonNull(env.getProperty("local.server.port")));

		String accessTokenUrl = String.format("http://localhost:%d/github/login/oauth/access_token", port);
		String profileUrl = String.format("http://localhost:%d/github/user", port);

		GithubUrlProperties githubUrlProperties = new GithubUrlProperties(accessTokenUrl, profileUrl);
		GithubClientProperties githubClientProperties = new GithubClientProperties("client-id", "client-secret");

		githubClient = new GithubClient(githubUrlProperties, githubClientProperties);
	}

	@DisplayName("github code로 accessToken 발급 테스트")
	@Test
	void requestGithubToken() {
		// given
		GithubResponses 사용자1 = GithubResponses.사용자1;

		// when
		String accessToken = githubClient.requestGithubToken(사용자1.getCode());

		// then
		assertThat(accessToken).isEqualTo(사용자1.getAccessToken());
	}

	@DisplayName("github accessToken으로 사용자 정보 받아오기")
	@Test
	void requestUser() {
		// given
		GithubResponses 사용자1 = GithubResponses.사용자1;

		// when
		GithubProfileResponse githubProfileResponse = githubClient.requestUser(사용자1.getAccessToken());

		// then
		assertThat(githubProfileResponse.getEmail()).isEqualTo(사용자1.getEmail());
	}
}
