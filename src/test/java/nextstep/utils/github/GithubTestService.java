package nextstep.utils.github;

import nextstep.auth.AuthenticationException;
import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.springframework.stereotype.Service;

@Service
public class GithubTestService {
    public GithubAccessTokenResponse accessToken(GithubAccessTokenRequest request) {
        GithubResponses githubResponses = GithubResponses.ofCode(request.getCode());
        return new GithubAccessTokenResponse(
                githubResponses.getAccessToken()
                , "bearer"
                , "scope1"
                , "");
    }

    public GithubProfileResponse getProfile(String authorization) {
        if (!"token".equalsIgnoreCase(authorization.split(" ")[0])) {
            throw new AuthenticationException();
        }

        String accessToken = authorization.split(" ")[1];
        GithubResponses githubResponses = GithubResponses.ofAceessToken(accessToken);

        return new GithubProfileResponse(githubResponses.getEmail(), githubResponses.getAge());
    }
}
