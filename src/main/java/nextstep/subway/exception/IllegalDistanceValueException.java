package nextstep.subway.exception;

import nextstep.subway.common.ErrorMessage;

public class IllegalDistanceValueException extends RuntimeException {

    public IllegalDistanceValueException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
