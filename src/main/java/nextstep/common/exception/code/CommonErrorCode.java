package nextstep.common.exception.code;

import org.springframework.http.HttpStatus;

public enum CommonErrorCode implements ErrorCode {
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
    FORBIDDEN(HttpStatus.FORBIDDEN),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST);

    private final HttpStatus status;

    CommonErrorCode(HttpStatus status) {
        this.status = status;
    }

    @Override
    public String code() {
        return name();
    }

    @Override
    public HttpStatus status() {
        return status;
    }
}
