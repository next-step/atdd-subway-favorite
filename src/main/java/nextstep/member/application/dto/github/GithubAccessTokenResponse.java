package nextstep.member.application.dto.github;

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
