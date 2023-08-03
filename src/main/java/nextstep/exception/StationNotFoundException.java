package nextstep.exception;

import static nextstep.exception.SubwayError.NOT_FOUND_STATION;

public class StationNotFoundException extends SubwayException {
    public StationNotFoundException() {
        super(NOT_FOUND_STATION);
    }
}
