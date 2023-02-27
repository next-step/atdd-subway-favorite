package nextstep.subway.unit;

import nextstep.member.application.GithubClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Profile("test")
@Component
@Primary
public class StubGithubClient implements GithubClient {

    @Override
    public String getAccessTokenFromGithub(String code) {
        return GithubSampleResponse.findByCode(code).getAccessToken();
    }
}
