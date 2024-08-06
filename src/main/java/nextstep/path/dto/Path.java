package nextstep.path.dto;

import nextstep.path.exception.PathException;
import nextstep.station.dto.StationResponse;
import nextstep.station.entity.Station;

import java.util.ArrayList;
import java.util.List;

import static nextstep.common.constant.ErrorCode.PATH_NOT_FOUND;

public class Path {
    private final List<Station> stations;
    private final double weight;

    public Path(List<Station> stations, double weight) {
        this.stations = stations;
        this.weight = weight;
    }

    public static Path of(final List<Station> stations, final double weight) {
        return new Path(stations, weight);
    }

    public PathResponse createPathResponse() {
        if (stations == null || stations.isEmpty()) {
            throw new PathException(String.valueOf(PATH_NOT_FOUND));
        }

        List<StationResponse> stationResponses = new ArrayList<>();
        for (Station station : stations) {
            stationResponses.add(StationResponse.of(station.getId(), station.getName()));
        }

        return PathResponse.of(stationResponses, weight);
    }

    public List<Station> getStations() {
        return stations;
    }

    public double getWeight() {
        return weight;
    }

}

