package nextstep.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MemberErrorMessage {
    UNAUTHORIZED("접근 권한이 없습니다."),
    NOT_FOUND_FAVORITE("즐겨찾기를 찾을 수 없습니다.");

    private final String message;
}
