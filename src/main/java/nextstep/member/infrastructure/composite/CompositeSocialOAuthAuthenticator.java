package nextstep.member.infrastructure.composite;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.command.authenticator.SocialOAuthAuthenticateCommand;
import nextstep.member.domain.command.authenticator.SocialOAuthAuthenticator;
import nextstep.member.domain.command.authenticator.SocialOAuthProvider;
import nextstep.member.domain.command.authenticator.SocialOAuthUser;
import nextstep.member.infrastructure.github.GithubAuthenticator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompositeSocialOAuthAuthenticator implements SocialOAuthAuthenticator {

    private final GithubAuthenticator githubAuthenticator;

    @Override
    public SocialOAuthUser authenticate(SocialOAuthAuthenticateCommand.ByAuthCode command) {
        if (command.getProvider() == SocialOAuthProvider.GITHUB) {
            return githubAuthenticator.authenticate(command);
        }

        throw new RuntimeException("에러");
    }
}
