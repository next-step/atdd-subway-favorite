package nextstep.subway.domain.exception;

public class NotFoundStationException extends SubwayDomainException {
    public NotFoundStationException(Long id) {
        super(SubwayDomainExceptionType.NOT_FOUND_STATION, "stationId " + id + " is invalid");
    }
}
