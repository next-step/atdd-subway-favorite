package nextstep.subway.exception;

import nextstep.subway.common.SubwayErrorMessage;

public class NotSameUpAndDownStationException extends RuntimeException {

    public NotSameUpAndDownStationException(SubwayErrorMessage subwayErrorMessage) {
        super(subwayErrorMessage.getMessage());
    }
}
