package nextstep.auth.application;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("github.client")
public class GithubClientProperties {

    private String clientId;
    private String clientSecret;

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
