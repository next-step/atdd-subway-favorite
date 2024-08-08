package nextstep.member.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GithubOAuthProperty {
    @Value("${oauth2.github.client-id}")
    private String clientId;
    @Value("${oauth2.github.client-secret}")
    private String clientSecret;
    @Value("${oauth2.github.user-info-uri}")
    private String userInfoUrl;
    @Value("${oauth2.github.token-uri}")
    private String tokenUrl;

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getUserInfoUrl() {
        return userInfoUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }
}
