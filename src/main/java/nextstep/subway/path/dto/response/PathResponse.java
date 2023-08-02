package nextstep.subway.path.dto.response;

import lombok.Builder;
import lombok.Getter;
import nextstep.subway.path.vo.Path;
import nextstep.subway.station.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class PathResponse {

    List<StationResponse> stations;

    Integer distance;

    public static PathResponse of(Path path) {
        List<StationResponse> stations = path.getStations()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return PathResponse.builder()
                .stations(stations)
                .distance(path.getDistance())
                .build();
    }

}
