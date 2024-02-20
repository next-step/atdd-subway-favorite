package nextstep.auth.client.github;

import nextstep.auth.client.ExternalTokenFetcher;
import nextstep.auth.client.dto.ProfileResponse;
import nextstep.auth.client.dto.ProfileResponseFactory;
import org.springframework.stereotype.Component;

@Component
public class GithubTokenFetcher implements ExternalTokenFetcher {
    private final GithubClient githubClient;

    public GithubTokenFetcher(GithubClient githubClient) {
        this.githubClient = githubClient;
    }


    @Override
    public String requestToken(String code) {
        return githubClient.requestToken(code);
    }

    @Override
    public ProfileResponse findUser(String accessToken) {
        return ProfileResponseFactory.create(githubClient.findUser(accessToken));
    }
}
