package nextstep.subway.fake;

import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.infrastructure.GithubClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class FakeGithubClient extends GithubClient {

    @Override
    public String getAccessTokenFromGithub(String code) {
        return FakeGithubResponse.getAccessToken(code);
    }

    @Override
    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        return FakeGithubResponse.findBy(accessToken);
    }

}
