package nextstep.member.application.dto;

public class GithubAccessTokenResponse {
    private String accessToken;
    private String scope;
    private String tokenType;

    public GithubAccessTokenResponse(String accessToken, String scope, String tokenType) {
        this.accessToken = accessToken;
        this.scope = scope;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
