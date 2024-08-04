package nextstep.station.application.exception;

public class NotExistStationException extends IllegalStateException {

    private static final String MESSAGE = "존재하지 않는 지하철 역입니다.";

    public NotExistStationException() {
        super(MESSAGE);
    }
}
