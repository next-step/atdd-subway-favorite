package nextstep.subway.applicaion.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Message {
    SOURCE_TARGET_DUPLICATE_STATION("Source 역와 Target 역이 중복되었습니다."),
    MEMBER_NOT_EXIST("회원이 존재하지 않습니다."),
    INVALID_FAVORITE_ID("잘못된 즐겨찾기 번호입니다."),
    UNAUTHORIZED_FAVORITE("해당 즐겨찾기에 권한이 없습니다."),
    ;
    private String message;
}
