package nextstep.station.domain.exception;

public class CantDeleteStationException extends RuntimeException {
    public CantDeleteStationException(String message) {
        super(message);
    }
}
