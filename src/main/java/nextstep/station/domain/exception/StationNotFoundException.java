package nextstep.station.domain.exception;

import nextstep.common.exception.exception.EntityNotFoundException;

public class StationNotFoundException extends EntityNotFoundException {
    public static final String ERROR_MESSAGE = "존재하지 않는 지하철역입니다. ID=%d";

    public StationNotFoundException(Long id) {
        super(String.format(ERROR_MESSAGE, id));
    }
}
