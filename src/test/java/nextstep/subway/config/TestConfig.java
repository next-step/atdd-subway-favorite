package nextstep.subway.config;

import nextstep.member.auth.Auth2Client;
import nextstep.subway.fake.FakeAuth2Client;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public Auth2Client auth2Client() {
        return new FakeAuth2Client();
    }
}
