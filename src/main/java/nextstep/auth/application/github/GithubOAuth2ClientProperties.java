package nextstep.auth.application.github;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "github")
@ConstructorBinding
public class GithubOAuth2ClientProperties {

    private final GithubClient client;
    private final GithubUrl url;


    public GithubOAuth2ClientProperties(final GithubClient client, final GithubUrl url) {
        this.client = client;
        this.url = url;
    }

    public String getClientId() {
        return client.id;
    }

    public String getClientSecret() {
        return client.secret;
    }

    public String getTokenUrl() {
        return url.accessToken;
    }

    public String getProfileUrl() {
        return url.profile;
    }

    public static class GithubClient {
        private final String id;
        private final String secret;

        public GithubClient(final String id, final String secret) {
            this.id = id;
            this.secret = secret;
        }
    }

    public static class GithubUrl {
        private final String accessToken;
        private final String profile;

        public GithubUrl(final String accessToken, final String profile) {
            this.accessToken = accessToken;
            this.profile = profile;
        }
    }
}
