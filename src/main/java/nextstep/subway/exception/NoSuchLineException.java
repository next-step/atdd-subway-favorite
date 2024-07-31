package nextstep.subway.exception;

public class NoSuchLineException extends RuntimeException {
    public NoSuchLineException(String message) {
        super(message);
    }
}
