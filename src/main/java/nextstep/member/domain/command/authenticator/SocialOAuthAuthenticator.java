package nextstep.member.domain.command.authenticator;

public interface SocialOAuthAuthenticator {
    SocialOAuthUser authenticate(SocialOAuthAuthenticateCommand.ByAuthCode command);
}
