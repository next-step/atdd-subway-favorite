package nextstep.github.application;

import nextstep.member.application.GithubClient;
import nextstep.member.application.dto.TokenResponse;
import nextstep.util.RandomToken;
import org.springframework.stereotype.Service;

@Service
public class GithubService {
    private GithubClient githubClient;

    public GithubService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public TokenResponse login(String code) {
        String accessToken = githubClient.getAccessTokenFromGithub(code);
        return TokenResponse.of(accessToken);
    }

    public TokenResponse join() {
        String accessToken = RandomToken.createRandomToken(15);
        return TokenResponse.of(accessToken);
    }
}
