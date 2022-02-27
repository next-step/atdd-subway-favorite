package nextstep.global.error;

import nextstep.global.error.exception.ErrorCode;

public class ErrorResponse {
    private final String message;
    private final String status;

    ErrorResponse(ErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
