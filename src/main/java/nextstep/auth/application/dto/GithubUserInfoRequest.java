package nextstep.auth.application.dto;

public class GithubUserInfoRequest {

    private final String accessToken;

    public GithubUserInfoRequest(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
