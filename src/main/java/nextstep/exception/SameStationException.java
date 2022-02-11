package nextstep.exception;

import nextstep.error.ErrorCode;

public class SameStationException extends NextStepException {

    public SameStationException() {
        super(ErrorCode.SAME_STATION_ERROR);
    }
}
