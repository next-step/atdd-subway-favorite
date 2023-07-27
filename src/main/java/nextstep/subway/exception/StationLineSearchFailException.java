package nextstep.subway.exception;

import nextstep.common.StationBusinessException;

public class StationLineSearchFailException extends StationBusinessException {
    public StationLineSearchFailException(String message) {
        super(message);
    }
}
