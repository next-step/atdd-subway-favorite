package atdd.path.auth;

public class JwtTokenDTO {
    String accessToken;
    String tokenType;

    public JwtTokenDTO(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}
