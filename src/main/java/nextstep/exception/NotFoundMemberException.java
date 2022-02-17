package nextstep.exception;

import nextstep.error.ErrorCode;

public class NotFoundMemberException extends NextStepException {
    public NotFoundMemberException() {
        super(ErrorCode.NOT_FOUND_MEMBER_ERROR);
    }
}
