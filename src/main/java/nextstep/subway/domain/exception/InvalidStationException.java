package nextstep.subway.domain.exception;

public class InvalidStationException extends SubwayDomainException {
    public InvalidStationException(Long id) {
        super(SubwayDomainExceptionType.INVALID_STATION, "stationId " + id + " is invalid");
    }

    public InvalidStationException(String detail) {
        super(SubwayDomainExceptionType.INVALID_STATION, detail);
    }
}
