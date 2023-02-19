package nextstep.subway.config;

import nextstep.member.ui.GithubOauthAdapter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class GithubOauthConfig {

    @Primary
    @Bean
    GithubOauthAdapter githubOauthAdapter() {
        return new MockGithubOauthAdapter();
    }
}
