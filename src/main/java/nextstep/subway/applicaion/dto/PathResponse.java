package nextstep.subway.applicaion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.domain.Path;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    public static PathResponse of(Path path) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int distance = path.extractDistance();

        return new PathResponse(stations, distance);
    }
}
