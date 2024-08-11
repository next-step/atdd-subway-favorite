package nextstep.common.exception;

import nextstep.common.response.ErrorCode;

public class InvalidUpSationException extends BusinessException {
    public InvalidUpSationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
