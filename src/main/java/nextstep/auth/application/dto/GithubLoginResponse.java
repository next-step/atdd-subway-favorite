package nextstep.auth.application.dto;

public class GithubLoginResponse {
    private final String accessToken;

    public GithubLoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
