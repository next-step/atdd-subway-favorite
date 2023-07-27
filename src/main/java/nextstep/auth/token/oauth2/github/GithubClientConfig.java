package nextstep.auth.token.oauth2.github;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class GithubClientConfig {

    @Bean
    @Profile("!test")
    public GithubClientInterface prod() {
        return new GithubClient();
    }
}
