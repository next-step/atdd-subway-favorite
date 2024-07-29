package nextstep.subway.exception;

import nextstep.subway.common.ErrorMessage;

public class NotSameUpAndDownStationException extends RuntimeException {

    public NotSameUpAndDownStationException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
