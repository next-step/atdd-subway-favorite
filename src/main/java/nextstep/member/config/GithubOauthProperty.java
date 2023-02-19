package nextstep.member.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "github")
public class GithubOauthProperty {

    private final Client client;

    private final Url url;

    public GithubOauthProperty(Client client, Url url) {
        this.client = client;
        this.url = url;
    }

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

    @ConstructorBinding
    @ConfigurationProperties(prefix = "github.client")
    static class Client {
        private final String id;

        private final String secret;

        public Client(String id, String secret) {
            this.id = id;
            this.secret = secret;
        }
    }

    @ConstructorBinding
    @ConfigurationProperties(prefix = "github.url")
    static class Url {

        private final String base;

        private final String accessToken;

        private final String profile;

        public Url(String base, String accessToken, String profile) {
            this.base = base;
            this.accessToken = accessToken;
            this.profile = profile;
        }
    }
}
