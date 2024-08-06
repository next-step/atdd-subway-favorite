package nextstep.auth.domain.command;

import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.entity.SocialOAuthUser;
import nextstep.auth.domain.entity.TokenPrincipal;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthCommander {
    private final EmailPasswordVerifier emailPasswordVerifier;
    private final SocialOAuthUserVerifier socialOAuthUserVerifier;
    private final SocialOAuthUserFetcher socialOAuthUserFetcher;
    private final TokenGenerator tokenGenerator;

    public String authenticateEmailPassword(String email, String password) {
        TokenPrincipal tokenPrincipal = emailPasswordVerifier.verify(email, password);
        return tokenGenerator.createToken(tokenPrincipal);
    }

    public String authenticateSocialOAuth(AuthenticateSocialOAuthCommand.ByAuthCode command) {
        SocialOAuthUser socialOAuthUser = socialOAuthUserFetcher.fetch(command);
        TokenPrincipal tokenPrincipal = socialOAuthUserVerifier.verify(socialOAuthUser);
        return tokenGenerator.createToken(tokenPrincipal);
    }
}
