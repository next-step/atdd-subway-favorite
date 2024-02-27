package nextstep.path.ui.dto;

import nextstep.path.application.dto.PathDto;
import nextstep.station.application.dto.StationResponseBody;

import java.util.List;

public class PathResponseBody {
    List<StationResponseBody> stations;
    int distance;

    private PathResponseBody(List<StationResponseBody> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<StationResponseBody> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public static PathResponseBody create(PathDto pathDto) {
        return new PathResponseBody(StationResponseBody.from(pathDto.getStations()), pathDto.getDistance());
    }
}
