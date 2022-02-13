package nextstep.auth.context;

import java.util.Objects;

public class SecurityContext {
    private Authentication authentication;

    public SecurityContext() {
    }

    public SecurityContext(Authentication authentication) {
        this.authentication = authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public boolean hasNotAuthentication() {
        return Objects.isNull(authentication);
    }
}
