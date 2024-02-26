package subway.unit.token;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import subway.acceptance.AcceptanceTest;
import subway.fixture.member.GithubResponses;
import subway.member.GithubClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GithubClientTest extends AcceptanceTest {
	@Autowired
	private GithubClient githubClient;

	// @BeforeEach
	// public void setUp(ConfigurableApplicationContext context) {
	// 	int port = super.port;
	// 	Map<String, String> properties =
	// 		Map.of(
	// 			"github.access-token.url",
	// 			String.format("%s%d%s", "http://localhost:", port, "/github/login/oauth/access_token")
	// 		);
	//
	// 	TestPropertyValues.of(properties).applyTo(context.getEnvironment());
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
}
