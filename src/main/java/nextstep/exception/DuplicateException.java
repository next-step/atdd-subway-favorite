package nextstep.exception;

import nextstep.error.ErrorCode;

public class DuplicateException extends NextStepException {

    public DuplicateException() {
        super(ErrorCode.DUPLICATE_ERROR);
    }
}
