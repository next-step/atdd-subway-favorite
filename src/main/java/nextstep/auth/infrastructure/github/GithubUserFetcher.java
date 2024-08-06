package nextstep.auth.infrastructure.github;

import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.command.AuthenticateSocialOAuthCommand;
import nextstep.auth.domain.command.SocialOAuthUserFetcher;
import nextstep.auth.domain.entity.SocialOAuthUser;
import nextstep.auth.infrastructure.github.dto.GithubAccessTokenResponse;
import nextstep.auth.infrastructure.github.dto.GithubProfileResponse;
import nextstep.auth.infrastructure.github.dto.GithubAccessTokenRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GithubUserFetcher implements SocialOAuthUserFetcher {

    private final GithubConfig githubConfig;
    private final GithubOAuthClient githubOAuthClient;
    private final GithubApiClient githubApiClient;

    @Override
    public SocialOAuthUser fetch(AuthenticateSocialOAuthCommand.ByAuthCode command) {
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
