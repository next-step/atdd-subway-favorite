package nextstep.member.common;

import lombok.Getter;

@Getter
public enum MemberErrorMessage {

    NOT_VALID_USER_CODE("올바른 인증 코드가 아닙니다.");

    private final String message;

    MemberErrorMessage(String message) {
        this.message = message;
    }
}
