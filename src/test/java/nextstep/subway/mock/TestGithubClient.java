package nextstep.subway.mock;

import nextstep.member.infra.GithubClient;
import nextstep.member.infra.dto.GithubProfileResponse;
import nextstep.subway.unit.GithubResponses;
import org.springframework.stereotype.Component;

@Component
public class TestGithubClient implements GithubClient {

    @Override
    public String getAccessTokenFromGithub(final String code) {
        if (code.equals(GithubResponses.사용자1.getCode())) {
            return GithubResponses.사용자1.getAccessToken();
        }
        if (code.equals(GithubResponses.사용자2.getCode())) {
            return GithubResponses.사용자2.getAccessToken();
        }
        if (code.equals(GithubResponses.사용자3.getCode())) {
            return GithubResponses.사용자3.getAccessToken();
        }
        if (code.equals(GithubResponses.사용자4.getCode())) {
            return GithubResponses.사용자4.getAccessToken();
        }
        throw new RuntimeException();
    }

    @Override
    public GithubProfileResponse getGithubProfileFromGithub(final String accessToken) {
        if (accessToken.equals(GithubResponses.사용자1.getAccessToken())) {
            return new GithubProfileResponse(GithubResponses.사용자1.getEmail());
        }
        if (accessToken.equals(GithubResponses.사용자2.getAccessToken())) {
            return new GithubProfileResponse(GithubResponses.사용자2.getEmail());
        }
        if (accessToken.equals(GithubResponses.사용자3.getAccessToken())) {
            return new GithubProfileResponse(GithubResponses.사용자3.getEmail());
        }
        if (accessToken.equals(GithubResponses.사용자4.getAccessToken())) {
            return new GithubProfileResponse(GithubResponses.사용자4.getEmail());
        }
        throw new RuntimeException();
    }
}
