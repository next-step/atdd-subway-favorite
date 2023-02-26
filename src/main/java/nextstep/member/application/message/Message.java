package nextstep.member.application.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Message {
    NOT_EXIST_EAMIL("존재하지 않는 Email 입니다."),
    INVALID_PASSWORD("잘못된 비밀번호 입니다."),
    NOT_EXIST_EMAIL_ID("존재하지 않은 ID 입니다."),
    INVALID_TOKEN("유효하지 않은 토큰입니다."),
    INVALID_CODE("유효하지 않은 코드입니다.")
    ;

    private String message;
}
