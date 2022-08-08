package nextstep.station.domain.exception;

public class CantDeleteStationException extends RuntimeException {
    public static final String BELONGS_TO_LINE = "노선에 포함된 지하철역을 삭제할 수 없습니다.";

    public CantDeleteStationException(String message) {
        super(message);
    }
}
