package nextstep.member.application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "github")
public class GithubProperties {
    public final Client client;
    public final Url url;

    public GithubProperties(Client client, Url url) {
        this.client = client;
        this.url = url;
    }

    public static class Client {
        public final String id;
        public final String secret;

        public Client(String id, String secret) {
            this.id = id;
            this.secret = secret;
        }
    }

    public static class Url {
        public final String accessToken;
        public final String profile;

        public Url(String accessToken, String profile) {
            this.accessToken = accessToken;
            this.profile = profile;
        }
    }
}
