package nextstep.auth.application.dto;

public class GithubTokenResponse {
    private String accessToken;

    protected GithubTokenResponse() {}

    public GithubTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
