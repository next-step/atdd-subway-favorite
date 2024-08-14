package nextstep.global.exception;

public enum ExceptionCode {

    NOT_FOUND_STATION(1001, "요청한 ID에 해당 역은 존재하지 않습니다."),
    NOT_FOUND_LINE(1002, "요청한 ID에 해당 노선은 존재하지 않습니다."),
    NOT_FOUND_MEMBER(1003, "해당 유저는 존재하지 않습니다"),
    NOT_FOUND_FAVORITE(1004, "해당 즐겨찾기는 존재하지 않습니다"),

    INVALID_DOWNSTATION_TO_BE_NEW_UPSTATION(2001, "기존 구간의 하행 종점역이 새로운 구간 상행역이 되어야 합니다."),
    INVALID_DOWNSTATION_NOT_NEW_EQUAL_DOWNSTATION(2002, "새로운 구간의 하행 종점역과 기존 구간의 하행 종점역은 같으면 안됩니다."),
    INVALID_SECTION_MIN(2003, "구간이 1개일 경우 삭제 할 수 없습니다."),
    INVALID_DELETE_DOWNSTATION(2004, "마지막 구간만 삭제 할 수 있습니다."),
    INVALID_NO_EXIST_SECTION(2005, "구간이 존재하지 않습니다."),
    INVALID_DUPLICATE_SECTION(2006, "해당 역은 이미 존재합니다."),

    INVALID_DUPLICATE_PATH(3001, "출발역과 도착역은 같을 수가 없습니다."),
    INVALID_CONNECT_PATH(3001, "출발역과 도착역은 연결되지 않은 역입니다.");

    private final int code;
    private final String message;

    ExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
