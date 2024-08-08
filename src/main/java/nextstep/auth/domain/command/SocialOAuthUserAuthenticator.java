package nextstep.auth.domain.command;

import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.entity.SocialOAuthUser;
import nextstep.auth.domain.entity.TokenPrincipal;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialOAuthUserAuthenticator {
    private final SocialOAuthUserVerifier socialOAuthUserVerifier;
    private final SocialOAuthUserFetcher socialOAuthUserFetcher;
    private final TokenGenerator tokenGenerator;

    public String authenticate(AuthenticateSocialOAuthCommand.ByAuthCode command) {
        SocialOAuthUser socialOAuthUser = socialOAuthUserFetcher.fetch(command);
        TokenPrincipal tokenPrincipal = socialOAuthUserVerifier.verify(socialOAuthUser);
        return tokenGenerator.createToken(tokenPrincipal);
    }
}
