package nextstep.member.tobe.application.dto;

public class TokenResponse {
    private final String accessToken;

    private TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public static TokenResponse of(String accessToken) {
        return new TokenResponse(accessToken);
    }

    public String getAccessToken() {
        return accessToken;
    }
}

