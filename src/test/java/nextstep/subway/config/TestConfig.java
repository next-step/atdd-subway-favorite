package nextstep.subway.config;

import nextstep.member.auth.OAuth2Client;
import nextstep.subway.fake.FakeOAuth2Client;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public OAuth2Client oAuth2Client() {
        return new FakeOAuth2Client();
    }
}
