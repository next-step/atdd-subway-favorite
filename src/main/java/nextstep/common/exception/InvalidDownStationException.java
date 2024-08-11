package nextstep.common.exception;

import nextstep.common.response.ErrorCode;

public class InvalidDownStationException extends BusinessException {
    public InvalidDownStationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
