package nextstep.auth.application;

import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.utils.fixture.GithubAuthFixture;
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

	private GithubAuthFixture 회원 = GithubAuthFixture.사용자1;

	@Test
	void 깃헙_토큰_요청() {
		String githubToken = githubClient.requestGithubToken(회원.getCode());

		assertThat(githubToken).isEqualTo(회원.getAccessToken());
	}

	@Test
	void 깃헙_프로필_요청() {
		GithubProfileResponse response = githubClient.requestGithubProfile(회원.getAccessToken());

		assertThat(response.getEmail()).isEqualTo(회원.getEmail());
	}
}
