package nextstep.auth;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
