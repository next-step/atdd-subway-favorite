package nextstep.auth.domain;

public class GithubAccessTokenResponse {

    private final String accessToken;

    public GithubAccessTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
