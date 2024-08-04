package nextstep.member.domain.command.authenticator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SocialOAuthUser {
    private SocialOAuthProvider provider;
    private String email;
}
