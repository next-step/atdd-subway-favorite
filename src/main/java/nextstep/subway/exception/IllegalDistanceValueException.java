package nextstep.subway.exception;

import nextstep.subway.common.SubwayErrorMessage;

public class IllegalDistanceValueException extends RuntimeException {

    public IllegalDistanceValueException(SubwayErrorMessage subwayErrorMessage) {
        super(subwayErrorMessage.getMessage());
    }
}
