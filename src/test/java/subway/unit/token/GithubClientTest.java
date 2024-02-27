package subway.unit.token;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import subway.dto.member.GithubProfileResponse;
import subway.fixture.member.GithubResponses;
import subway.member.GithubClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GithubClientTest {
	@Autowired
	private GithubClient githubClient;

	// todo: DEFINED_PORT가 아닌 RANDOM_PORT로 설정해보기
	// @BeforeEach
	// public void setUp(@Autowired ConfigurableApplicationContext context) {
	// 	int port = super.port;
	//
	// 	Map<String, String> properties =
	// 		Map.of(
	// 			"github.url.access-token",
	// 			String.format("%s%d%s", "http://localhost:", port, "/github/login/oauth/access_token"),
	// 			"github.url.profile",
	// 			String.format("%s%d%s", "http://localhost:", port, "/github/user"),
	// 			"github.client.id", "client-id",
	// 			"github.client.secret", "client-secret"
	// 		);
	//
	// 	TestPropertyValues.of(properties).applyTo(context);
	// }

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
