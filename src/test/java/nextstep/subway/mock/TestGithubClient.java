package nextstep.subway.mock;

import nextstep.member.infra.GithubClient;
import nextstep.member.infra.dto.GithubProfileResponse;
import org.springframework.stereotype.Component;

import static nextstep.subway.unit.GithubResponses.getAccessTokenFromCode;
import static nextstep.subway.unit.GithubResponses.getEmailFromAccessToken;

@Component
public class TestGithubClient implements GithubClient {

    @Override
    public String getAccessTokenFromGithub(final String code) {
        return getAccessTokenFromCode(code);
    }

    @Override
    public GithubProfileResponse getGithubProfileFromGithub(final String accessToken) {
        return new GithubProfileResponse(getEmailFromAccessToken(accessToken));
    }
}
