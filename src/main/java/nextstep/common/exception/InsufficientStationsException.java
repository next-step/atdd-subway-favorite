package nextstep.common.exception;

import nextstep.common.response.ErrorCode;

public class InsufficientStationsException extends BusinessException {
    public InsufficientStationsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
