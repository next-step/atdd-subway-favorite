package nextstep.member.infrastructure.github;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.command.authenticator.SocialOAuthAuthenticateCommand;
import nextstep.member.domain.command.authenticator.SocialOAuthAuthenticator;
import nextstep.member.domain.command.authenticator.SocialOAuthUser;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GithubAuthenticator implements SocialOAuthAuthenticator {

    private final GithubConfig githubConfig;
    private final GithubOAuthClient githubOAuthClient;

    @Override
    public SocialOAuthUser authenticate(SocialOAuthAuthenticateCommand.ByAuthCode command) {
        return null;
    }
}
