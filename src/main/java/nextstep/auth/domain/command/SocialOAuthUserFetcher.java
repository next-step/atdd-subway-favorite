package nextstep.auth.domain.command;

import nextstep.auth.domain.entity.SocialOAuthUser;

public interface SocialOAuthUserFetcher {
    SocialOAuthUser fetch(AuthenticateSocialOAuthCommand.ByAuthCode command);
}
