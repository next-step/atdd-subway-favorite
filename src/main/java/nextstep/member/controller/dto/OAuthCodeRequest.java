package nextstep.member.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.member.domain.command.authenticator.AuthenticateSocialOAuthCommand;
import nextstep.member.domain.command.authenticator.SocialOAuthProvider;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OAuthCodeRequest {
    private String code;

    public AuthenticateSocialOAuthCommand.ByAuthCode toCommand(SocialOAuthProvider provider) {
        return new AuthenticateSocialOAuthCommand.ByAuthCode(provider, code);
    }
}
