package nextstep.common.exception;

import nextstep.common.response.ErrorCode;

public class SameStationException extends BusinessException {
    public SameStationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public SameStationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
