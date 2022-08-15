package nextstep.auth.token;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class TokenResponse {
    private String accessToken;

    public TokenResponse() {
    }

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
