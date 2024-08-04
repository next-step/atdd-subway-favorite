package nextstep.member.domain.command.authenticator;

public interface SocialOAuthAuthenticator {
    SocialOAuthUser authenticate(AuthenticateSocialOAuthCommand.ByAuthCode command);
}
