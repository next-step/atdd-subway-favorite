package nextstep.auth.model.context;

public class SecurityContext {
    private Authentication authentication;

    public SecurityContext() {
    }

    private SecurityContext(Authentication authentication) {
        this.authentication = authentication;
    }

    public static SecurityContext from(Authentication authentication) {
        return new SecurityContext(authentication);
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public Authentication getAuthentication() {
        return authentication;
    }
}