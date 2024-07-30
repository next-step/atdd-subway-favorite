package nextstep.member.application.dto;

public class GithubAccessTokenResponse {
    private String accessToken;
    private String tokenType;
    private String scope;

    public GithubAccessTokenResponse(String accessToken, String tokenType, String scope) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.scope = scope;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getScope() {
        return scope;
    }
}
