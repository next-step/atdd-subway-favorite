package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.LineStation;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LineServiceTest {
    private List<Line> lines;
    private List<Station> stations;

    @BeforeEach
    void setUp() {
        Station station1 = new Station("강남역");
        ReflectionTestUtils.setField(station1, "id", 1L);
        Station station2 = new Station("역삼역");
        ReflectionTestUtils.setField(station2, "id", 2L);
        Station station3 = new Station("양재역");
        ReflectionTestUtils.setField(station3, "id", 3L);
        stations = Lists.newArrayList(station1, station2, station3);

        Line line1 = new Line("신분당선", "RED", LocalTime.now(), LocalTime.now(), 10);
        ReflectionTestUtils.setField(line1, "id", 1L);
        Line line2 = new Line("2호선", "GREEN", LocalTime.now(), LocalTime.now(), 10);
        ReflectionTestUtils.setField(line2, "id", 2L);
        lines = Lists.newArrayList(line1, line2);

        line1.addLineStation(new LineStation(1L, null, 10, 10));
        line1.addLineStation(new LineStation(3L, 1L, 10, 10));
        line2.addLineStation(new LineStation(1L, null, 10, 10));
        line2.addLineStation(new LineStation(2L, 1L, 10, 10));
    }

    @Test
    void findAllLinesWithStations() {
        LineRepository lineRepository = mock(LineRepository.class);
        StationRepository stationRepository = mock(StationRepository.class);
        LineService lineService = new LineService(lineRepository, stationRepository);

        when(lineRepository.findAll()).thenReturn(lines);
        when(stationRepository.findAllById(anyList())).thenReturn(stations);

        List<LineResponse> result = lineService.findAllLinesWithStations();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getStations()).hasSize(2);
        assertThat(result.get(1).getStations()).hasSize(2);
    }
}
