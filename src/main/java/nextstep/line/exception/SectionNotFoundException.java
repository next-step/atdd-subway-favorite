package nextstep.line.exception;

import nextstep.exceptions.BaseException;
import nextstep.exceptions.ErrorMessage;

public class SectionNotFoundException extends BaseException {
    public SectionNotFoundException(final ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
