package nextstep.common.response;

public enum ErrorCode {
    // Section Add
    INVALID_UP_STATION_ADD(400, "S001", " 새로운 구간의 상행역이 해당 노선에 등록된 하행 종점역이 아닙니다."),
    INVALID_DOWN_STATION_ADD(400, "S002", " 새로운 구간의 하행 종점역이 해당 노선이 포함되어있습니다."),
    INVALID_STATION_ADD(400, "S005", " 해당 역이 이미 노선에 포함되어 있습니다."),
    TOO_LONG_DISTANCE_ADD(400, "S006", " 추가되는 구간의 거리는 기존 구간의 거리보다 작아야합니다."),
    INVALID_DISTANCE_ADD(400, "S007", " 추가되는 구간의 거리는 1이상이어야 합니다."),

    // Section Delete
    INSUFFICIENT_STATION_DELETE(400, "S003", " 지하철 노선에 상행 종점역과 하행 종점역만 있습니다."),
    NOT_LAST_STATION_DELETE(400, "S004", " 지하철 노선에 등록된 역(하행 종점역)이 아닙니다."),

    // Section View
    NOT_FOUND_FIRST_SECTION(400, "S008", " 첫번째 구간을 찾을 수 없습니다."),

    // Path
    NOT_FOUND_PATH(400, "S009", " 경로를 찾을 수 없습니다."),
    NOT_FOUND_STATION(400, "S010", " 출발역 또는 도착역이 존재하지 않습니다."),
    SAME_STATION(400, "S011", " 출발역 또는 도착역이 동일합니다.");


    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
