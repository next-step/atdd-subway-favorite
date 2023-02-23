package nextstep.auth.application.fake;

import nextstep.auth.application.GithubClient;
import nextstep.auth.application.dto.GithubProfileResponse;
import org.springframework.stereotype.Component;

@Component
public class FakeGithubClient extends GithubClient {

    @Override
    public String getAccessTokenFromGithub(String code) {
        return GithubResponses.getGithubResponseByCode(code).getAccessToken();
    }

    @Override
    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        return GithubProfileResponse.of(GithubResponses.getGithubResponseByAccessToken(accessToken));
    }
}
