package nextstep.subway.auth.infrastructure;

import nextstep.subway.auth.domain.Authentication;

public class SecurityContext {

    public static final SecurityContext EMPTY_CONTEXT = new SecurityContext();

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
}