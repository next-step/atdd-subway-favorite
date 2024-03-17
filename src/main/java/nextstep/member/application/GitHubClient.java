package nextstep.member.application;

import org.springframework.stereotype.Component;

@Component
public class GitHubClient {
    public String requestGitHubAccessToken(String code) {
        return code + " github-access-token";
    }
}
