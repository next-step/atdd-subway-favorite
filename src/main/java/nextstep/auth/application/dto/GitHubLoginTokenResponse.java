package nextstep.auth.application.dto;

public class GitHubLoginTokenResponse {

    private final String accessToken;

    public GitHubLoginTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
