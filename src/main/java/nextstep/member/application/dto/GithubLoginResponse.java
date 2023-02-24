package nextstep.member.application.dto;

public class GithubLoginResponse {

    private final String accessToken;

    public GithubLoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public static GithubLoginResponse from(GithubAccessTokenResponse githubLogin) {
        return new GithubLoginResponse(githubLogin.getAccessToken());
    }

    public String getAccessToken() {
        return accessToken;
    }

}
