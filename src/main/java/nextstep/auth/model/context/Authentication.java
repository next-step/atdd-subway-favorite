package nextstep.auth.model.context;

import nextstep.auth.model.authentication.UserDetails;

public class Authentication {
    private UserDetails principal;

    public Authentication() {
    }

    public Authentication(UserDetails principal) {
        this.principal = principal;
    }

    public UserDetails getPrincipal() {
        return principal;
    }
}
