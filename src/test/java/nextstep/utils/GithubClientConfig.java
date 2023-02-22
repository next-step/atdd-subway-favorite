package nextstep.utils;

import nextstep.member.application.GithubClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class GithubClientConfig {

    @Bean
    public GithubClient githubClient() {
        return new FakeGithubClientImpl();
    }
}
