package nextstep.subway.exception;

import nextstep.subway.common.ErrorMessage;

public class NoStationException extends RuntimeException {

    public NoStationException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
