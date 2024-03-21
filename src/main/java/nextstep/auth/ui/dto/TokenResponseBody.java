package nextstep.auth.ui.dto;

public class TokenResponseBody {
    private String accessToken;

    protected TokenResponseBody() {
    }

    public TokenResponseBody(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public static TokenResponseBody create(String accessToken) {
        return new TokenResponseBody(accessToken);
    }
}
