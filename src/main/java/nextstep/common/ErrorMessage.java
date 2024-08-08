package nextstep.common;

import lombok.Getter;

@Getter
public enum ErrorMessage {

    SERVER_ERROR("서버에 에러가 발생했습니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
