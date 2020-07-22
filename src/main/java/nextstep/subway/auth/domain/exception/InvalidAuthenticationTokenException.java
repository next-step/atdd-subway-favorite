package nextstep.subway.auth.domain.exception;

public class InvalidAuthenticationTokenException extends RuntimeException {
    public InvalidAuthenticationTokenException(String message) {
        super(message);
    }
}
