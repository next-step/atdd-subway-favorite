package subway.path.application.dto;

import lombok.Builder;
import lombok.Getter;
import subway.station.application.dto.StationResponse;

import java.util.List;

@Getter
@Builder
public class PathRetrieveResponse {
    private List<StationResponse> stations;
    private long distance;
}
