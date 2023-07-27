package nextstep.oauth2;

import nextstep.auth.token.oauth2.github.GithubClientInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class TestGithubClientTestConfig {


    @Bean
    @Profile("test")
    public GithubClientInterface test() {
        return new TestGithubClient();
    }
}
