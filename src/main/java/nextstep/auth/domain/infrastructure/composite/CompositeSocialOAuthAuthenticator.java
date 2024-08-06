package nextstep.auth.domain.infrastructure.composite;

import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.command.AuthenticateSocialOAuthCommand;
import nextstep.auth.domain.command.SocialOAuthAuthenticator;
import nextstep.auth.domain.entity.SocialOAuthProvider;
import nextstep.auth.domain.entity.SocialOAuthUser;
import nextstep.auth.domain.exception.AuthDomainException;
import nextstep.auth.domain.exception.AuthDomainExceptionType;
import nextstep.auth.domain.infrastructure.github.GithubAuthenticator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class CompositeSocialOAuthAuthenticator implements SocialOAuthAuthenticator {

    private final GithubAuthenticator githubAuthenticator;

    @Override
    public SocialOAuthUser authenticate(AuthenticateSocialOAuthCommand.ByAuthCode command) {
        if (command.getProvider() == SocialOAuthProvider.GITHUB) {
            return githubAuthenticator.authenticate(command);
        }

        throw new AuthDomainException(AuthDomainExceptionType.INVALID_SOCIAL_LOGIN_PROVIDER);
    }
}
