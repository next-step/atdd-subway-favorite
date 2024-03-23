package nextstep.auth.application.dto;

public class GitHubAccessTokenResponse {

    private String accessToken;

    public GitHubAccessTokenResponse() {
    }

    public GitHubAccessTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
