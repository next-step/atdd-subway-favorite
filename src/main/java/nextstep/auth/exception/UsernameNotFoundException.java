package nextstep.auth.exception;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException() {
        super("not found UserDetails by username");
    }
}
