package nextstep.subway.utils;

import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.auth.GithubClient;
import org.springframework.stereotype.Component;

@Component
public class FakeGithubClient extends GithubClient {
    @Override
    public String getAccessTokenFromGithub(String code) {
        return FakeGithubResponse.findAccessTokenByCode(code);
    }

    @Override
    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        return new GithubProfileResponse(FakeGithubResponse.findEmailByAccessToken(accessToken));
    }
}
