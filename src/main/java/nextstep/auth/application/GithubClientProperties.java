package nextstep.auth.application;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("github.client")
public class GithubClientProperties {

    private String clientId;
    private String clientSecret;

    private String url;

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getUrl() {
        return url;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
