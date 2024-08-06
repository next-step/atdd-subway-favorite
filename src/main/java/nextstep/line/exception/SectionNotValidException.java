package nextstep.line.exception;

import nextstep.exceptions.BaseException;
import nextstep.exceptions.ErrorMessage;

public class SectionNotValidException extends BaseException {
    public SectionNotValidException(final ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
