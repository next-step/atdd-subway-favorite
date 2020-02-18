package atdd.path.application.dto;

import java.util.StringJoiner;

public class LoginResponseView {

    private String tokenType;
    private String accessToken;

    protected LoginResponseView() {}

    public LoginResponseView(String tokenType, String accessToken) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LoginResponseView.class.getSimpleName() + "[", "]")
                .add("tokenType='" + tokenType + "'")
                .add("accessToken='" + accessToken + "'")
                .toString();
    }

}
