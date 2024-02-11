package nextstep.station.exception;


import nextstep.common.exception.NotFoundException;

public class StationNotExistException extends NotFoundException {
    public StationNotExistException(final Long id) {
        super(String.format("Station is not exist - id : %s", id));
    }
}
