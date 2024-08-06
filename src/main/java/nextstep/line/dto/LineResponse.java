package nextstep.line.dto;

import nextstep.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private Long distance;
    private List<StationResponse> stations = new ArrayList<>();

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, Long distance, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.stations = stations;
    }

    public static LineResponse of(final String name, final String color, final Long distance) {
        return new LineResponse(null, name, color, distance, new ArrayList<>());
    }

    public static LineResponse of(final String name, final String color, final Long distance, final List<StationResponse> stations) {
        return new LineResponse(null, name, color, distance, stations);
    }

    public static LineResponse of(final Long id, final String name, final String color, final Long distance) {
        return new LineResponse(id, name, color, distance, new ArrayList<>());
    }

    public static LineResponse of(final Long id, final String name, final String color, final Long distance, final List<StationResponse> stations) {
        return new LineResponse(id, name, color, distance, stations);
    }

    public void addStationResponses(final List<StationResponse> stationResponses) {
        for (StationResponse stationResponse : stationResponses) {
            this.stations.add(stationResponse);
        }
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }

    public Long getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return this.stations;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LineResponse that = (LineResponse) obj;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(color, that.color) &&
                Objects.equals(distance, that.distance) &&
                Objects.equals(stations, that.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, distance, stations);
    }

}


