package nextstep.subway.unit;

import nextstep.subway.line.SectionFixtures;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.service.LineService;
import nextstep.subway.station.StationFixtures;
import nextstep.subway.station.StationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class LineServiceMockTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationService stationService;

    @InjectMocks
    private LineService lineService;

    @Test
    void addSection() {
        // given
        // lineRepository, stationService stub 설정을 통해 초기값 셋팅
        Line line = new Line("name", "color", SectionFixtures.FIRST_SECTION);
        Mockito.doReturn(Optional.of(line)).when(lineRepository).findByIdWithSectionsAndStations(1L);
        Mockito.doReturn(StationFixtures.FIRST_DOWN_STATION).when(stationService).findById(2L);
        Mockito.doReturn(StationFixtures.SECOND_UP_STATION).when(stationService).findById(3L);

        // when
        // lineService.addSection 호출
        LineResponse lineResponse = lineService.addSection(1L, new SectionRequest(2L, 3L, 20L));

        // then
        // lineService.findLineById 메서드를 통해 검증
        Line foundedLine = lineService.findLineById(1L);
        Assertions.assertThat(foundedLine.getSections()).hasSize(2);
    }
}
