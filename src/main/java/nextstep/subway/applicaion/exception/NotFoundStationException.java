package nextstep.subway.applicaion.exception;

import nextstep.common.exception.ErrorCode;

public class NotFoundStationException extends SubwayException {

    public NotFoundStationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
