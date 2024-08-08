package nextstep.auth.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SocialOAuthUser {
    private SocialOAuthProvider provider;
    private String email;
}
