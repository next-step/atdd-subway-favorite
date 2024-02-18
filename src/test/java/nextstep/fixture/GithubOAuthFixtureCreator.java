package nextstep.fixture;

import nextstep.client.github.dto.GithubAccessTokenResponse;
import nextstep.utils.GithubResponses;

/**
 * @author : Rene Choi
 * @since : 2024/02/18
 */
public class GithubOAuthFixtureCreator {
	public static GithubAccessTokenResponse createDefaultGithubAccessTokenResponse(GithubResponses response) {
		return GithubAccessTokenResponse.of(response.getCode());
	}
}
