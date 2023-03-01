package nextstep.global.error.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    MISMATCH_BEARER_PREFIX_OF_JWT_TOKEN(HttpStatus.BAD_REQUEST.value(), "Bearer 토큰이 존재하지 않습니다.");

    private int status;
    private String errorMessage;

    ErrorCode(int status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
