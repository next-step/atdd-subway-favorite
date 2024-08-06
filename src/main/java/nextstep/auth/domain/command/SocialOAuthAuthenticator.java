package nextstep.auth.domain.command;

import nextstep.auth.domain.entity.SocialOAuthUser;

public interface SocialOAuthAuthenticator {
    SocialOAuthUser authenticate(AuthenticateSocialOAuthCommand.ByAuthCode command);
}
