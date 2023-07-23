package nextstep.api.auth.aop.principal;

import lombok.Getter;

@Getter
public class UserPrincipal {
    private final String username;
    private final String role;

    public UserPrincipal(final String username, final String role) {
        this.username = username;
        this.role = role;
    }
}
