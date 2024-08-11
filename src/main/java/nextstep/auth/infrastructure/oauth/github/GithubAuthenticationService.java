package nextstep.auth.infrastructure.oauth.github;

import lombok.AllArgsConstructor;
import nextstep.auth.infrastructure.oauth.github.dto.GithubAccessTokenRequest;
import nextstep.auth.dto.TokenResponse;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GithubAuthenticationService {

    private final GithubClient githubClient;

    public TokenResponse authenticate(GithubAccessTokenRequest request) {

        String accessToken = githubClient.requestGithubAeccessToken(request.getCode());
        return new TokenResponse(accessToken);
    }
}
