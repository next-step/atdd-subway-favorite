package nextstep.member.domain.exception;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
    }

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(Throwable cause) {
        super(cause);
    }
}
