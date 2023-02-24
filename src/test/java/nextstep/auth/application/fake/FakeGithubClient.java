package nextstep.auth.application.fake;

import nextstep.auth.application.GithubClient;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.application.dto.GithubResponses;
import org.springframework.stereotype.Component;

@Component
public class FakeGithubClient extends GithubClient {

    @Override
    public String getAccessTokenFromGithub(String code) {
        return FakeGithubResponses.getGithubResponseByCode(code).getAccessToken();
    }

    @Override
    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        FakeGithubResponses fakeGithubResponses = FakeGithubResponses.getGithubResponseByAccessToken(accessToken);

        GithubResponses githubResponses = new GithubResponses(fakeGithubResponses.getEmail());
        return GithubProfileResponse.of(githubResponses);
    }
}
