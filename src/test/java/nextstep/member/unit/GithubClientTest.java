package nextstep.member.unit;

import nextstep.member.application.GithubClient;
import nextstep.member.application.dto.GithubProfileResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {
	@Autowired
	private GithubClient githubClient;

	@Test
	void 깃헙_토큰_요청() {
		String code = "code";

		String githubToken = githubClient.requestGithubToken(code);

		assertThat(githubToken).isEqualTo("accessToken");
	}

	@Test
	void 깃헙_프로필_요청() {
		String accessToken = "accessToken";

		GithubProfileResponse response = githubClient.requestGithubProfile(accessToken);

		assertThat(response.getEmail()).isEqualTo("email@email.com");
	}
}
