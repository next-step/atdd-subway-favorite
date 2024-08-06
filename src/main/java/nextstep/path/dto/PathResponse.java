package nextstep.path.dto;

import nextstep.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class PathResponse {

    private List<StationResponse> stationResponses = new ArrayList<>();
    private Double distance;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stationResponses, Double distance) {
        this.stationResponses = stationResponses;
        this.distance = distance;
    }

    public static PathResponse of(final List<StationResponse> stationResponses, final Double distance) {
        return new PathResponse(stationResponses, distance);
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }

    public Double getDistance() {
        return distance;
    }

}

