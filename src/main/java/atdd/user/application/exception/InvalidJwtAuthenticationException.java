package atdd.user.application.exception;

public class InvalidJwtAuthenticationException extends RuntimeException  {
    public InvalidJwtAuthenticationException(String message) {
        super(message);
    }
}
