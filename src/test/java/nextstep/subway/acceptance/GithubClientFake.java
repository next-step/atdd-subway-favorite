package nextstep.subway.acceptance;

import nextstep.infra.github.GithubClient;
import nextstep.infra.github.dto.GithubProfileResponse;

public class GithubClientFake extends GithubClient {

    @Override
    public String getAccessTokenFromGithub(String code) {
        return GithubTestResponses.accessTokenFromCode(code);
    }

    @Override
    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        GithubTestResponses githubTestResponses = GithubTestResponses.of(accessToken);

        return new GithubProfileResponse(
            githubTestResponses.getAccessToken(),
            githubTestResponses.getEmail()
        );
    }
}
