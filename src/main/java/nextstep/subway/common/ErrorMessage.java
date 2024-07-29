package nextstep.subway.common;

import lombok.Getter;

@Getter
public enum ErrorMessage {

    CANNOT_DELETE_SECTION("구간을 삭제할 수 없습니다."),
    NO_LINE_EXIST("해당하는 지하철 노선 정보가 없습니다."),
    NO_STATION_EXIST("해당하는 지하철 역 정보가 없습니다."),
    NOT_SAME_UP_AND_DOWN_STATION("구간 정보가 올바르지 않습니다."),
    ILLEGAL_DISTANCE_VALUE("거리 간격이 0 이하일 수 없습니다."),
    LARGE_DISTANCE_THAN_CURRENT_SECTION("기존 구간의 거리보다 클 수 없습니다."),
    CANNOT_ADD_STATION("새로운 역을 추가할 수 없습니다."),
    NOT_CONNECTED_STATION("연결되어 있는 역이 아닙니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
