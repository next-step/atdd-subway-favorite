package nextstep.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("github.client")
public class GitHubClientProperties {

    private String baseUrl;
    private String id;
    private String secret;

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getId() {
        return id;
    }

    public String getSecret() {
        return secret;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
