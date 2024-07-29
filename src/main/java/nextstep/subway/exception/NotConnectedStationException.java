package nextstep.subway.exception;

import nextstep.subway.common.ErrorMessage;

public class NotConnectedStationException extends RuntimeException {

    public NotConnectedStationException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
