package atdd.path.application.dto;

import atdd.path.domain.Station;

import java.util.List;

public class PathResponseView {
    private Long startStationId;
    private Long endStationId;
    private List<StationResponseView> stations;

    public PathResponseView() {
    }

    public PathResponseView(Long startStationId, Long endStationId) {
        this.startStationId = startStationId;
        this.endStationId = endStationId;
    }

    public PathResponseView(Long startStationId, Long endStationId, List<Station> stations) {
        this.startStationId = startStationId;
        this.endStationId = endStationId;
        this.stations = StationResponseView.listOf(stations);
    }

    public Long getStartStationId() {
        return startStationId;
    }

    public Long getEndStationId() {
        return endStationId;
    }

    public List<StationResponseView> getStations() {
        return stations;
    }
}
