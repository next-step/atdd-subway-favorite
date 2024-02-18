package nextstep.fixture;

import nextstep.api.auth.domain.dto.inport.GithubCodeResponse;
import nextstep.utils.GithubMockResponses;

/**
 * @author : Rene Choi
 * @since : 2024/02/18
 */
public class GithubOAuthFixtureCreator {
	public static GithubCodeResponse createDefaultGithubCodeResponse(GithubMockResponses response) {
		return GithubCodeResponse.of(response.getCode());
	}
}
