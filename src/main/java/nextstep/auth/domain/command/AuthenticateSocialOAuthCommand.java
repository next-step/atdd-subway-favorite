package nextstep.auth.domain.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.auth.domain.entity.SocialOAuthProvider;

public class AuthenticateSocialOAuthCommand {

    @Getter
    @AllArgsConstructor
    public static class ByAuthCode {
        private SocialOAuthProvider provider;
        private String code;
    }
}
