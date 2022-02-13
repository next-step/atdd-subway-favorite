package nextstep.auth.authentication;

public class UsernamePasswordAuthenticationToken extends AuthenticationToken {
    public UsernamePasswordAuthenticationToken(String principal, String credentials) {
        super(principal, credentials);
    }
}
