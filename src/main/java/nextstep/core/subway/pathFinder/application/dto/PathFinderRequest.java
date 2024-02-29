package nextstep.core.subway.pathFinder.application.dto;

public class PathFinderRequest {

    Long departureStationId;

    Long arrivalStationId;

    public PathFinderRequest(Long departureStationId, Long arrivalStationId) {
        this.departureStationId = departureStationId;
        this.arrivalStationId = arrivalStationId;
    }

    public Long getDepartureStationId() {
        return departureStationId;
    }

    public Long getArrivalStationId() {
        return arrivalStationId;
    }
}
