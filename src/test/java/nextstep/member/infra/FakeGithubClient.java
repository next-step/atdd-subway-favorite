package nextstep.member.infra;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.GithubResponses;
import nextstep.member.domain.GithubClient;

@Component
@Primary
public class FakeGithubClient implements GithubClient {
    @Override
    public GithubAccessTokenResponse getAccessToken(GithubAccessTokenRequest request) {
        GithubResponses response = GithubResponses.findByCode(request.getCode());
        return new GithubAccessTokenResponse(response.getAccessToken(), "bearer", "repo,user");
    }

    @Override
    public GithubProfileResponse getUserProfile(String accessToken) {
        GithubResponses response = GithubResponses.findByAccessToken(accessToken);
        return new GithubProfileResponse(response.getEmail(), 30);
    }
}

