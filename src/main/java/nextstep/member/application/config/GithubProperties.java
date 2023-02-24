package nextstep.member.application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "github")
public class GithubProperties {
    private final Client client;
    private final Url url;

    public GithubProperties(Client client, Url url) {
        this.client = client;
        this.url = url;
    }

    public Client getClient() {
        return client;
    }

    public Url getUrl() {
        return url;
    }

    public static class Client {
        private final String id;
        private final String secret;

        public Client(String id, String secret) {
            this.id = id;
            this.secret = secret;
        }

        public String getId() {
            return id;
        }

        public String getSecret() {
            return secret;
        }
    }

    public static class Url {
        private final String accessToken;
        private final String profile;

        public Url(String accessToken, String profile) {
            this.accessToken = accessToken;
            this.profile = profile;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public String getProfile() {
            return profile;
        }
    }
}
