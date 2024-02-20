package nextstep.auth.application.dto;

public class GithubAccessTokenResponse {
    private String accessToken;

    public GithubAccessTokenResponse() {
    }

    public GithubAccessTokenResponse(final String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
