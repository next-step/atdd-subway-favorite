package nextstep.line.exception;


import nextstep.exceptions.BaseException;
import nextstep.exceptions.ErrorMessage;

public class InvalidDownStationException extends BaseException {
    public InvalidDownStationException(final ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
