package nextstep.exception;

import nextstep.error.ErrorCode;

public class NotConnectedException extends NextStepException {

    public NotConnectedException() {
        super(ErrorCode.NOT_CONNECTED_ERROR);
    }
}
