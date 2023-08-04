package nextstep.exception;

import static nextstep.exception.SubwayError.STATION_NOT_EXIST_EXCEPTION;

public class StationNotExistException extends SubwayException {
    public StationNotExistException() {
        super(STATION_NOT_EXIST_EXCEPTION);
    }
}
