package nextstep.subway.fake;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import nextstep.GithubAccountFixtures;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.service.GithubClient;

@Component
@Profile("test")
public class GithubFakeClient extends GithubClient {

    @Override
    public String getAccessTokenFromGithub(String code) {
        return GithubAccountFixtures.findByCode(code)
            .getAccessToken();
    }

    @Override
    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        String email = GithubAccountFixtures
            .findByAccessToken(accessToken)
            .getEmail();
        return GithubProfileResponse.of(email);
    }
}
