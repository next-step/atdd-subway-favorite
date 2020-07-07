package nextstep.subway.line.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class LineServiceTest {
    @Test
    void findAllLinesWithStations() {
        LineRepository lineRepository = mock(LineRepository.class);
        StationRepository stationRepository = mock(StationRepository.class);
        LineService lineService = new LineService(lineRepository, stationRepository);

        List<LineResponse> result = lineService.findAllLinesWithStations();

        assertThat(result).hasSize(1);
    }
}
