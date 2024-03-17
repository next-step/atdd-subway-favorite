package nextstep.member.application.dto;

public class GitHubAccessTokenResponse {

    private final String accessToken;

    public GitHubAccessTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
