package nextstep.subway.auth.domain;

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
