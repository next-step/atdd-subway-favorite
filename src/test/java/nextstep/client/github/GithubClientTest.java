package nextstep.client.github;

import static nextstep.utils.GithubMockResponses.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import nextstep.api.CommonAcceptanceTest;
import nextstep.client.github.dto.GithubUserProfileResponse;

/**
 * @author : Rene Choi
 * @since : 2024/02/17
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest extends CommonAcceptanceTest {

	@Autowired
	private GithubClient githubClient;

	/**
	 * given - 사용자 code
	 * when - request github server to accessToken (which will be used fetching userinfo afterwards)
	 * then - access token from github
	 */
	@Test
	@DisplayName("사용자의 code가 주어질 때 깃허브 인증 서버에 토큰을 요청하고 깃허브 서버는 AccessToken을 응답한다")
	void requestGithubToken(){
		// given
		String code = 사용자1.getCode();

		// when
		String githubToken = githubClient.requestGithubToken(code).getAccessToken();

		// then
		assertThat(githubToken).isNotBlank();
		assertThat(githubToken).isEqualTo(사용자1.getAccessToken());
	}

	/**
	 * given - access token from github
	 * when - request user info (oauth user)
	 * then - returns user info
	 * c.f. -> see https://docs.github.com/ko/rest/users/users?apiVersion=2022-11-28#get-the-authenticated-user
	 */
	@Test
	@DisplayName("사용자의 access token이 주어질 때, 사용자 프로필 정보를 가져온다")
	void requestGithubUserProfile() {
		// given
		String accessToken = 사용자1.getAccessToken();

		// when
		GithubUserProfileResponse userProfile = githubClient.requestGithubUserProfile(accessToken);

		// then
		assertThat(userProfile).isNotNull();
		assertThat(userProfile.getEmail()).isEqualTo(사용자1.getEmail());
		assertThat(userProfile.getId()).isEqualTo(사용자1.getId());
		assertThat(userProfile.getLogin()).isEqualTo(사용자1.getLogin());
	}

}
