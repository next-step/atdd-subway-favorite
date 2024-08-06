package nextstep.auth.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.auth.domain.command.AuthenticateSocialOAuthCommand;
import nextstep.auth.domain.entity.SocialOAuthProvider;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthCodeRequest {
    private String code;

    public AuthenticateSocialOAuthCommand.ByAuthCode toCommand(SocialOAuthProvider provider) {
        return new AuthenticateSocialOAuthCommand.ByAuthCode(provider, code);
    }
}
