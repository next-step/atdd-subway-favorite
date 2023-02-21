package nextstep.github.application.dto;

public class GithubAccessTokenResponse {
    private String accessToken;

    private GithubAccessTokenResponse() {
    }

    public GithubAccessTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
