package nextstep.line.exception;

import nextstep.exceptions.BaseException;
import nextstep.exceptions.ErrorMessage;

public class InsufficientStationsException extends BaseException {
    public InsufficientStationsException(final ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
