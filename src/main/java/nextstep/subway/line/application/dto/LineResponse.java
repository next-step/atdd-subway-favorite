package nextstep.subway.line.application.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.station.application.dto.StationResponse;

@Getter
@AllArgsConstructor
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
}
