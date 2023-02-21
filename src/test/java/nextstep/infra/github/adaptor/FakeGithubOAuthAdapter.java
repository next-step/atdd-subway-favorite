package nextstep.infra.github.adaptor;

import nextstep.infra.github.dto.GithubProfileResponse;
import nextstep.infra.github.mock.GithubFakeResponses;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class FakeGithubOAuthAdapter implements GithubOAuthAdapter {

    @Override
    public GithubProfileResponse login(String code) {
        GithubFakeResponses githubFakeResponse = GithubFakeResponses.fromCode(code);

        return new GithubProfileResponse(
            githubFakeResponse.getAccessToken(), githubFakeResponse.getEmail()
        );
    }
}
