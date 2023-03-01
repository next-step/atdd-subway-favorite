package nextstep.subway.applicaion.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Message {
    SOURCE_TARGET_DUPLICATE_STATION("Source 역와 Target 역이 중복되었습니다.")
    ;
    private String message;
}
