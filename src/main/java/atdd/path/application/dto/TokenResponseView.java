package atdd.path.application.dto;

import java.io.Serializable;

public class TokenResponseView implements Serializable {
    private static final long serialVersionUID = -4240148918375694588L;

    private String accessToken;
    private String tokenType;

    public TokenResponseView() {
    }

    public TokenResponseView(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public static TokenResponseView of(String accessToken, String tokenType) {
        return new TokenResponseView(accessToken, tokenType);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}
