package nextstep.subway.member.client.github;

import nextstep.subway.member.client.ExternalTokenFetcher;
import nextstep.subway.member.client.dto.ProfileResponse;
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
        return null;
    }
}
