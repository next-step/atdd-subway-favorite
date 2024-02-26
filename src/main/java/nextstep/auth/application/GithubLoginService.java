package nextstep.auth.application;

import nextstep.auth.application.dto.GitHubLoginRequest;
import nextstep.auth.application.dto.GithubLoginResponse;
import org.springframework.stereotype.Service;

@Service
public class GithubLoginService {

    private final GithubClient githubClient;

    public GithubLoginService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public GithubLoginResponse login(GitHubLoginRequest request) {
        return new GithubLoginResponse(githubClient.requestGithubToken(request.getCode()));
    }
}
