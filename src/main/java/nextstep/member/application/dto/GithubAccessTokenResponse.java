package nextstep.member.application.dto;

public class GithubAccessTokenResponse {

    private String accessToken;

    public GithubAccessTokenResponse() {
    }

    public GithubAccessTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public GithubAccessTokenResponse(String accessToken, String s, String s1, String s2) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
