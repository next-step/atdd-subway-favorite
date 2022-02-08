package nextstep.auth.token;

public class TokenResponse {
    private String accessToken;

    public TokenResponse() {
    }

    public static TokenResponse of(String accessToken) {
        return new TokenResponse(accessToken);
    }

    private TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
