package nextstep.subway.exception;

import nextstep.subway.common.SubwayErrorMessage;

public class NotConnectedStationException extends RuntimeException {

    public NotConnectedStationException(SubwayErrorMessage subwayErrorMessage) {
        super(subwayErrorMessage.getMessage());
    }
}
