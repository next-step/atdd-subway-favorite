package nextstep.member.infrastructure.github;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.command.authenticator.SocialOAuthAuthenticateCommand;
import nextstep.member.domain.command.authenticator.SocialOAuthAuthenticator;
import nextstep.member.domain.command.authenticator.SocialOAuthUser;
import nextstep.member.infrastructure.github.dto.GithubAccessTokenRequest;
import nextstep.member.infrastructure.github.dto.GithubAccessTokenResponse;
import nextstep.member.infrastructure.github.dto.GithubProfileResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GithubAuthenticator implements SocialOAuthAuthenticator {

    private final GithubConfig githubConfig;
    private final GithubOAuthClient githubOAuthClient;
    private final GithubApiClient githubApiClient;

    @Override
    public SocialOAuthUser authenticate(SocialOAuthAuthenticateCommand.ByAuthCode command) {
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
