package nextstep.station.ui;

import nextstep.common.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public enum StationErrorCode implements ErrorCode {
    CANT_DELETE_STATION(HttpStatus.BAD_REQUEST);

    private final HttpStatus httpStatus;

    StationErrorCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String code() {
        return name();
    }

    @Override
    public HttpStatus status() {
        return httpStatus;
    }
}
