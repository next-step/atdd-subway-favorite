package nextstep.auth.domain.infrastructure.github;

import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.command.AuthenticateSocialOAuthCommand;
import nextstep.auth.domain.command.SocialOAuthAuthenticator;
import nextstep.auth.domain.entity.SocialOAuthUser;
import nextstep.auth.domain.infrastructure.github.dto.GithubAccessTokenResponse;
import nextstep.auth.domain.infrastructure.github.dto.GithubProfileResponse;
import nextstep.auth.domain.infrastructure.github.dto.GithubAccessTokenRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GithubAuthenticator implements SocialOAuthAuthenticator {

    private final GithubConfig githubConfig;
    private final GithubOAuthClient githubOAuthClient;
    private final GithubApiClient githubApiClient;

    @Override
    public SocialOAuthUser authenticate(AuthenticateSocialOAuthCommand.ByAuthCode command) {
        GithubAccessTokenRequest tokenRequest = new GithubAccessTokenRequest(
                githubConfig.getClientId(),
                githubConfig.getClientSecret(),
                command.getCode()
        );
        GithubAccessTokenResponse tokenResponse = githubOAuthClient.getAccessToken(tokenRequest);

        GithubProfileResponse profileResponse = githubApiClient.getUser(tokenResponse.getAccessToken());
        return new SocialOAuthUser(command.getProvider(), profileResponse.getEmail());
    }
}
