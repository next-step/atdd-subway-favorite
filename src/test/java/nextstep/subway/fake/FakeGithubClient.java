package nextstep.subway.fake;

import org.springframework.stereotype.Component;

import nextstep.member.authentication.github.GithubClient;
import nextstep.member.authentication.github.dto.GithubProfileResponse;

@Component
public class FakeGithubClient extends GithubClient {
    @Override
    public String getAccessTokenFromGithub(String code) {
        return FakeGithubResponses.findAccessToken(code);
    }

    @Override
    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        return new GithubProfileResponse(FakeGithubResponses.findEmail(accessToken));
    }
}
