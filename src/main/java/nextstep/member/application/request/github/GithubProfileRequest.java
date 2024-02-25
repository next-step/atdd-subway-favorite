package nextstep.member.application.request.github;

public class GithubProfileRequest {

    private String accessToken;

    private GithubProfileRequest() {
    }

    private GithubProfileRequest(String accessToken) {
        this.accessToken = accessToken;
    }

    public static GithubProfileRequest from(String accessToken) {
        return new GithubProfileRequest(accessToken);
    }

    public String getAccessToken() {
        return accessToken;
    }

}
