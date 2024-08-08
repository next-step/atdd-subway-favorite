package nextstep.common.exception;

import nextstep.common.response.ErrorCode;

public class NotLastStationException extends BusinessException {
    public NotLastStationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
