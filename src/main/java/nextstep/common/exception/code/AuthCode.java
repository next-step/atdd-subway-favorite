package nextstep.common.exception.code;

import lombok.Getter;

@Getter
public enum AuthCode implements ResponseCode {
    AUTH_INVALID(1002, "올바르지 않는 사용자입니다.");

    private final int code;

    private final String message;

    AuthCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
