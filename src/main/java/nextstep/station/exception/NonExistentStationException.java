package nextstep.station.exception;

import nextstep.exceptions.BaseException;
import nextstep.exceptions.ErrorMessage;

public class NonExistentStationException extends BaseException {

    public NonExistentStationException(final ErrorMessage errorMessage, final Long id) {
        super(errorMessage.getFormattingMessage(id));
    }

}
