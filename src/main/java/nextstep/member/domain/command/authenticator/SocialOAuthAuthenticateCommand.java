package nextstep.member.domain.command.authenticator;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class SocialOAuthAuthenticateCommand {

    @Getter
    @AllArgsConstructor
    public static class ByAuthCode {
        private SocialOAuthProvider provider;
        private String code;
    }
}
