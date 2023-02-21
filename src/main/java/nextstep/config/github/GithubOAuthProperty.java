package nextstep.config.github;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "github")
@RequiredArgsConstructor
public class GithubOAuthProperty {
    private final Client client;

    private final Url url;

    private final Timeout timeout;

    public String getClientId() {
        return this.client.id;
    }

    public String getClientSecret() {
        return this.client.secret;
    }

    public String getTokenUrl() {
        return this.url.accessToken;
    }

    public String getProfileUrl() {
        return this.url.profile;
    }

    public String getBaseUrl() {
        return this.url.base;
    }

    public int getConnectionTimeout() {
        return this.timeout.connection;
    }

    public int getWriteTimeout() {
        return this.timeout.write;
    }

    public int getReadTimeout() {
        return this.timeout.read;
    }

    @ConstructorBinding
    @ConfigurationProperties(prefix = "github.client")
    @RequiredArgsConstructor
    static class Client {
        private final String id;

        private final String secret;
    }

    @ConstructorBinding
    @ConfigurationProperties(prefix = "github.url")
    @RequiredArgsConstructor
    static class Url {

        private final String base;

        private final String accessToken;

        private final String profile;
    }

    @ConstructorBinding
    @ConfigurationProperties(prefix = "github.timeout")
    @RequiredArgsConstructor
    static class Timeout {
        private final int connection;

        private final int write;

        private final int read;
    }
}
