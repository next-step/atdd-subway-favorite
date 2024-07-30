package nextstep.member.infra;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.domain.GithubClient;

@Component
@Primary
public class FakeGithubClient implements GithubClient {
    @Override
    public GithubAccessTokenResponse getAccessTokenFromGithub(GithubAccessTokenRequest request) {
        return new GithubAccessTokenResponse("fake_access_token", "bearer", "repo,user");
    }

    @Override
    public GithubProfileResponse getUserProfileFromGithub(String accessToken) {
        return new GithubProfileResponse("email@example.com", 30);
    }
}
