package nextstep.station.exception;

import nextstep.exceptions.BaseException;
import nextstep.exceptions.ErrorMessage;

public class NonExistentStationException extends BaseException {

    public NonExistentStationException(final ErrorMessage errorMessage) {
        super(errorMessage);
    }

}
