package nextstep.line.ui;

import nextstep.common.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public enum LineErrorCode implements ErrorCode {
    ILLEGAL_SECTION_OPERATION(HttpStatus.BAD_REQUEST)
    ;

    private final HttpStatus httpStatus;

    LineErrorCode(HttpStatus httpStatus) {
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
