package nextstep.subway.utils;

import nextstep.member.application.GithubClient;
import nextstep.member.application.dto.GithubProfileResponse;
import org.springframework.stereotype.Component;

@Component
public class FakeGithubClient extends GithubClient {

    @Override
    public String getAccessTokenFromGithub(final String code) {
        return FakeGithubTokenResponse.getTokenByCode(code);
    }

    @Override
    public GithubProfileResponse getGithubProfileFromGithub(final String accessToken) {
        return super.getGithubProfileFromGithub(accessToken);
    }
}
