package nextstep.line.exception;

import nextstep.exceptions.BaseException;
import nextstep.exceptions.ErrorMessage;

public class SectionDistanceNotValidException extends BaseException {
    public SectionDistanceNotValidException(final ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
