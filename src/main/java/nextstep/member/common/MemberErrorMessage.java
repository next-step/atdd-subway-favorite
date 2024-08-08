package nextstep.member.common;

import lombok.Getter;

@Getter
public enum MemberErrorMessage {

    NOT_EXIST_MEMBER("존재하지 않는 사용자입니다.");

    private final String message;

    MemberErrorMessage(String message) {
        this.message = message;
    }
}
