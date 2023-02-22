package nextstep.member.infrastructure;

import nextstep.member.domain.GithubClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Profile("test")
@Component
public class FakeGithubClient implements GithubClient {

    @Override
    public Optional<String> getAccessTokenFromGithub(String code) {
        return Arrays.stream(GithubResponse.values())
            .filter(githubResponse -> code.equals(githubResponse.getCode()))
            .findFirst()
            .map(GithubResponse::getAccessToken);
    }
}
