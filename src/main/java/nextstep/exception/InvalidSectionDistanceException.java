package nextstep.exception;

public class InvalidSectionDistanceException extends RuntimeException {
    public InvalidSectionDistanceException(String message) {
        super(message);
    }

    public InvalidSectionDistanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
