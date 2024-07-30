package nextstep.subway.exception;

import nextstep.subway.common.SubwayErrorMessage;

public class AlreadyHasUpAndDownStationException extends RuntimeException {

    public AlreadyHasUpAndDownStationException(SubwayErrorMessage subwayErrorMessage) {
        super(subwayErrorMessage.getMessage());
    }
}
