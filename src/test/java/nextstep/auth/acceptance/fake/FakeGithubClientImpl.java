package nextstep.auth.acceptance.fake;

import nextstep.auth.application.GithubClient;
import nextstep.auth.application.dto.GithubProfileResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Primary
@Profile("test")
public class FakeGithubClientImpl implements GithubClient {
    @Override
    public String getAccessTokenFromGithub(String code) {
        return FakeGithubResponses.findAccessTokenByCode(code);
    }

    @Override
    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        return FakeGithubResponses.findEmailByAccessToken(accessToken);
    }
}
