package nextstep.auth.domain.command;

import nextstep.auth.domain.entity.SocialOAuthUser;
import nextstep.auth.domain.entity.TokenPrincipal;

public interface SocialOAuthUserVerifier {
    TokenPrincipal verify(SocialOAuthUser socialOAuthUser);
}
