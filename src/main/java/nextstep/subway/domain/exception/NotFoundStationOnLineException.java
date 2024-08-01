package nextstep.subway.domain.exception;

public class NotFoundStationOnLineException extends SubwayDomainException {
    public NotFoundStationOnLineException(String detail) {
        super(SubwayDomainExceptionType.NOT_FOUND_STATION_ON_LINE, detail);
    }
}
