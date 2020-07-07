package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LineServiceTest {
    @Test
    void findAllLinesWithStations() {
        LineRepository lineRepository = mock(LineRepository.class);
        StationRepository stationRepository = mock(StationRepository.class);
        LineService lineService = new LineService(lineRepository, stationRepository);

        when(lineRepository.findAll()).thenReturn(Lists.newArrayList(new Line(), new Line()));

        List<LineResponse> result = lineService.findAllLinesWithStations();

        assertThat(result).hasSize(2);
    }
}
