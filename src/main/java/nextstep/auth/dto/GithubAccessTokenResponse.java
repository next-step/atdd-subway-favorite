package nextstep.auth.dto;

public class GithubAccessTokenResponse {
    private String accessToken;

    public GithubAccessTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return this.accessToken;
    }
}
