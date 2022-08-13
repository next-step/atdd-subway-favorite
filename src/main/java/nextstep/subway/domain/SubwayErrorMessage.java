package nextstep.subway.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubwayErrorMessage {
    NOT_FOUND_STATION("등록된 역을 찾을 수 없습니다.");

    private final String message;
}
