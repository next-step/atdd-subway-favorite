package nextstep.subway.path.domain;

import com.google.common.collect.Lists;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineStationResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathResultTest {
    private List<LineResponse> lines;
    private LineStationResponse lineStationResponse6;
    private LineStationResponse lineStationResponse7;

    private StationResponse stationResponse1;
    private StationResponse stationResponse2;
    private StationResponse stationResponse3;
    private StationResponse stationResponse4;

    @BeforeEach
    void setUp() {
        stationResponse1 = new StationResponse(1L, "교대역", LocalDateTime.now(), LocalDateTime.now());
        stationResponse2 = new StationResponse(2L, "강남역", LocalDateTime.now(), LocalDateTime.now());
        stationResponse3 = new StationResponse(3L, "양재역", LocalDateTime.now(), LocalDateTime.now());
        stationResponse4 = new StationResponse(4L, "남부터미널역", LocalDateTime.now(), LocalDateTime.now());

        LineStationResponse lineStationResponse1 = new LineStationResponse(stationResponse1, null, 2, 2);
        LineStationResponse lineStationResponse2 = new LineStationResponse(stationResponse2, 1L, 2, 2);

        LineStationResponse lineStationResponse3 = new LineStationResponse(stationResponse2, null, 2, 2);
        LineStationResponse lineStationResponse4 = new LineStationResponse(stationResponse3, 2L, 2, 2);

        LineStationResponse lineStationResponse5 = new LineStationResponse(stationResponse1, null, 2, 2);
        lineStationResponse6 = new LineStationResponse(stationResponse4, 1L, 1, 2);
        lineStationResponse7 = new LineStationResponse(stationResponse3, 4L, 2, 2);

        LineResponse lineDetailResponse1 = new LineResponse(1L, "2호선", "GREEN", LocalTime.now(), LocalTime.now(), 5, Lists.newArrayList(lineStationResponse1, lineStationResponse2), LocalDateTime.now(), LocalDateTime.now());
        LineResponse lineDetailResponse2 = new LineResponse(2L, "신분당선", "RED", LocalTime.now(), LocalTime.now(), 5, Lists.newArrayList(lineStationResponse3, lineStationResponse4), LocalDateTime.now(), LocalDateTime.now());
        LineResponse lineDetailResponse3 = new LineResponse(3L, "3호선", "ORANGE", LocalTime.now(), LocalTime.now(), 5, Lists.newArrayList(lineStationResponse5, lineStationResponse6, lineStationResponse7), LocalDateTime.now(), LocalDateTime.now());

        lines = Lists.newArrayList(lineDetailResponse1, lineDetailResponse2, lineDetailResponse3);
    }

    @Test
    void extractLineStationResponses() {
        PathResult pathResult = new PathResult(Lists.newArrayList(1L, 4L, 3L), 3);

        List<LineStationResponse> lineStationResponses = pathResult.extractLineStationResponses(lines);

        assertThat(lineStationResponses).containsExactlyElementsOf(Lists.newArrayList(lineStationResponse6, lineStationResponse7));
    }

    @Test
    void extractStationResponse() {
        PathResult pathResult = new PathResult(Lists.newArrayList(1L, 4L, 3L), 3);

        List<StationResponse> stationResponses = pathResult.extractStationResponse(lines);

        assertThat(stationResponses).containsExactlyElementsOf(Lists.newArrayList(stationResponse1, stationResponse4, stationResponse3));
    }
}
