package nextstep.github.application;

import nextstep.github.application.dto.GithubAccessTokenRequest;
import nextstep.github.application.dto.GithubAccessTokenResponse;
import nextstep.github.application.dto.GithubProfileResponse;
import nextstep.github.domain.GithubResponses;
import nextstep.github.exception.GithubException;
import org.springframework.stereotype.Service;

@Service
public class GithubService {
    public String getCode(String email) {
        return findByEmail(email).getCode();
    }

    public GithubAccessTokenResponse getAccessToken(GithubAccessTokenRequest githubAccessTokenRequest) {
        String accessToken = findByCode(githubAccessTokenRequest.getCode()).getAccessToken();
        return new GithubAccessTokenResponse(accessToken);
    }

    public GithubProfileResponse getProfile(String accessToken) {
        GithubResponses responses = findByAccessToken(accessToken);
        return new GithubProfileResponse(responses.getEmail(), responses.getAge());
    }

    private GithubResponses findByEmail(String email) {
        return GithubResponses.findByEmail(email).orElseThrow(GithubException::new);
    }

    private GithubResponses findByCode(String code) {
        return GithubResponses.findByCode(code).orElseThrow(GithubException::new);
    }

    private GithubResponses findByAccessToken(String accessToken) {
        return GithubResponses.findByAccessToken(accessToken).orElseThrow(GithubException::new);
    }
}
