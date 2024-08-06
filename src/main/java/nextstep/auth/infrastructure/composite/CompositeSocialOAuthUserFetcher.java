package nextstep.auth.infrastructure.composite;

import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.command.AuthenticateSocialOAuthCommand;
import nextstep.auth.domain.command.SocialOAuthUserFetcher;
import nextstep.auth.domain.entity.SocialOAuthProvider;
import nextstep.auth.domain.entity.SocialOAuthUser;
import nextstep.auth.domain.exception.AuthDomainException;
import nextstep.auth.domain.exception.AuthDomainExceptionType;
import nextstep.auth.infrastructure.github.GithubUserFetcher;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class CompositeSocialOAuthUserFetcher implements SocialOAuthUserFetcher {

    private final GithubUserFetcher githubAuthenticator;

    @Override
    public SocialOAuthUser fetch(AuthenticateSocialOAuthCommand.ByAuthCode command) {
        if (command.getProvider() == SocialOAuthProvider.GITHUB) {
            return githubAuthenticator.fetch(command);
        }

        throw new AuthDomainException(AuthDomainExceptionType.INVALID_SOCIAL_LOGIN_PROVIDER);
    }
}
