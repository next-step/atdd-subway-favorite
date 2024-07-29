package nextstep.subway.exception;

import nextstep.subway.common.ErrorMessage;

public class AlreadyHasUpAndDownStationException extends RuntimeException {

    public AlreadyHasUpAndDownStationException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
