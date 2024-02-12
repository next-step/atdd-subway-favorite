package nextstep.path;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.station.StationResponse;

import java.util.List;

@Getter
@AllArgsConstructor
public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
}
