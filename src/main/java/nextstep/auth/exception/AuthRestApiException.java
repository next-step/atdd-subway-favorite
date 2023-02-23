package nextstep.auth.exception;

public class AuthRestApiException extends RuntimeException{
    public AuthRestApiException() {
    }

    public AuthRestApiException(String message) {
        super(message);
    }
}
