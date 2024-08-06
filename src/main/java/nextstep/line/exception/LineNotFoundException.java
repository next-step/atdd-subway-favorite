package nextstep.line.exception;


import nextstep.exceptions.BaseException;
import nextstep.exceptions.ErrorMessage;

public class LineNotFoundException extends BaseException {

    public LineNotFoundException(final ErrorMessage errorMessage) {
        super(errorMessage);
    }

}
