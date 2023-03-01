package nextstep.subway;

import nextstep.member.infra.GithubClient;
import nextstep.subway.mock.TestGithubClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class GithubClientTestContextConfiguration {

    @Bean
    public GithubClient githubClient() {
        return new TestGithubClient();
    }
}
