package nextstep.subway.exception;

public class NoSuchStationException extends RuntimeException {
    public NoSuchStationException(String message) {
        super(message);
    }
}
