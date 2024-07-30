package nextstep.subway.exception;

import nextstep.subway.common.SubwayErrorMessage;

public class NoStationException extends RuntimeException {

    public NoStationException(SubwayErrorMessage subwayErrorMessage) {
        super(subwayErrorMessage.getMessage());
    }
}
