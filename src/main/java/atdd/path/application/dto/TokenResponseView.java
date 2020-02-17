package atdd.path.application.dto;

import java.io.Serializable;

public class TokenResponseView implements Serializable {
    private static final long serialVersionUID = -4240148918375694588L;

    private String accessToken;
    private String tokenType = "bearer";

    public TokenResponseView() {
    }

    public TokenResponseView(String accessToken) {
        this.accessToken = accessToken;
    }

    public static TokenResponseView of(String accessToken) {
        return new TokenResponseView(accessToken);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}
