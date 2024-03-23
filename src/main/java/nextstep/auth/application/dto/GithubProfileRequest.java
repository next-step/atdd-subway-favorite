package nextstep.auth.application.dto;

public class GithubProfileRequest {

    private String accessToken;

    public GithubProfileRequest() {
    }

    public GithubProfileRequest(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
