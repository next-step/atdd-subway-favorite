package nextstep.subway.auth.dto;

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

    public static TokenResponse of(String accessToken){
        return new TokenResponse(accessToken);
    }
}