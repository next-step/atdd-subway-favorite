package nextstep.exception;

import static nextstep.exception.SubwayError.SECTION_DISTANCE_OVER_EXCEPTION;

public class SectionDistanceOverException extends SubwayException {
    public SectionDistanceOverException() {
        super(SECTION_DISTANCE_OVER_EXCEPTION);
    }
}
