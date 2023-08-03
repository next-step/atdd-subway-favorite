package nextstep.exception;

import static nextstep.exception.SubwayError.SHORTPATH_SAME_STATION;

public class ShortPathSameStationException extends SubwayException {
    public ShortPathSameStationException() {
        super(SHORTPATH_SAME_STATION);
    }
}
