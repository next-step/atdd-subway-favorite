package nextstep.exception;

import nextstep.error.ErrorCode;

public class NotFoundLineException extends NextStepException {

    public NotFoundLineException() {
        super(ErrorCode.NOT_FOUND_LINE_ERROR);
    }
}
