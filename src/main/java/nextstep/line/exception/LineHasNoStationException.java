package nextstep.line.exception;

import nextstep.exceptions.BaseException;
import nextstep.exceptions.ErrorMessage;

public class LineHasNoStationException extends BaseException {

    public LineHasNoStationException(final ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
