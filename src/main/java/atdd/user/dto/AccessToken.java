package atdd.user.dto;

import java.util.Objects;

public class AccessToken {

    private String accessToken;
    private String tokenType;

    private AccessToken() { }

    public static AccessToken of(String accessToken, TokenType tokenType) {
        AccessToken token = new AccessToken();
        token.accessToken = accessToken;
        token.tokenType = tokenType.getTypeName();
        return token;
    }

    public static AccessToken ofBearerToken(String accessToken) {
        return of(accessToken, TokenType.BEARER);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccessToken)) return false;
        AccessToken token = (AccessToken) o;
        return Objects.equals(accessToken, token.accessToken) &&
                Objects.equals(tokenType, token.tokenType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, tokenType);
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }

}
