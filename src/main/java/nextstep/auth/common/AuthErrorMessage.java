package nextstep.auth.common;

import lombok.Getter;

@Getter
public enum AuthErrorMessage {

    NOT_VALID_USER_CODE("올바른 인증 코드가 아닙니다.");

    private final String message;

    AuthErrorMessage(String message) {
        this.message = message;
    }
}
