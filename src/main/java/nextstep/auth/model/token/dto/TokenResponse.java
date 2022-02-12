package nextstep.auth.model.token.dto;

public class TokenResponse {
    private String accessToken;

    public TokenResponse() {
    }

    private TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public static TokenResponse from(String accessToken) {
        return new TokenResponse(accessToken);
    }

    public String getAccessToken() {
        return accessToken;
    }
}
