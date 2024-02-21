package nextstep.path.application.dto;

import nextstep.path.domain.Path;
import nextstep.station.application.dto.StationDto;

import java.util.List;

public class PathDto {
    private List<StationDto> stations;
    private int distance;

    public PathDto(List<StationDto> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<StationDto> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public static PathDto from(Path path) {
        return new PathDto(StationDto.from(path.getStations()), path.getDistance());
    }
}
