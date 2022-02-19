package nextstep.auth.token;

public class TokenResponse {
    private String accessToken;

    private TokenResponse() {
    }

    protected TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public static TokenResponse of(String accessToken) {
        return new TokenResponse(accessToken);
    }

    public String getAccessToken() {
        return accessToken;
    }
}
