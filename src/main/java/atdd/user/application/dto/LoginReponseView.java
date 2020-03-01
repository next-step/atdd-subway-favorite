package atdd.user.application.dto;

public class LoginReponseView {
    private String accessToken;
    private String tokenType;

    public LoginReponseView(String accessToken, String tokenType) {
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
