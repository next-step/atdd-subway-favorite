package nextstep.member.application.dto;

public class GithubLoginResponse {

    private final String accessToken;

    public GithubLoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public static GithubLoginResponse from(String accessToken) {
        return new GithubLoginResponse(accessToken);
    }

    public String getAccessToken() {
        return accessToken;
    }

}
