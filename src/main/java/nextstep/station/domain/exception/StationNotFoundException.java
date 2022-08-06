package nextstep.station.domain.exception;

import nextstep.common.exception.exception.EntityNotFoundException;

public class StationNotFoundException extends EntityNotFoundException {
    public StationNotFoundException() {
        super("지하철역을 찾을 수 없습니다.");
    }
}
