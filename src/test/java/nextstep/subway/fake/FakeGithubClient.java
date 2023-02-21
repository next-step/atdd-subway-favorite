package nextstep.subway.fake;

import nextstep.member.application.GithubClient;
import nextstep.member.application.dto.GithubProfileResponse;
import org.springframework.stereotype.Component;

@Component
public class FakeGithubClient extends GithubClient {

    @Override
    public String getAccessTokenFromGithub(String code) {
        return FakeGithubResponse.getAccessToken(code);
    }

    @Override
    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        return FakeGithubResponse.getProfile(accessToken);
    }
}
