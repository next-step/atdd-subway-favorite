package nextstep.unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import nextstep.line.domain.Line;
import nextstep.line.domain.Sections;
import nextstep.line.infrastructure.LineRepository;
import nextstep.station.application.StationService;
import nextstep.station.domain.Station;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LineServiceMockTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationService stationService;

    @Test
    void addSection() {
        // given
        // lineRepository, stationService stub 설정을 통해 초기값 셋팅
        Long 신사역_id = 1L;
        Long 강남역_id = 2L;
        Long 신분당선_id = 1L;

        Line 신분당선 = new Line(신분당선_id, "신분당선", "bg-red-600");
        Station 신사역 = new Station(신사역_id, "신사역");
        Station 강남역 = new Station(강남역_id, "강남역");

        Mockito.lenient().when(lineRepository.findById(신분당선_id)).thenReturn(Optional.of(신분당선));
        Mockito.lenient().when(stationService.findById(신사역_id)).thenReturn(신사역);
        Mockito.lenient().when(stationService.findById(강남역_id)).thenReturn(강남역);

        신분당선.addSection(신사역, 강남역, 7);

        // when
        // lineService.addSection 호출
        Long 논현역_id = 3L;
        Station 논현역 = new Station(논현역_id, "논현역");
        신분당선.addSection(신사역, 논현역, 7);

        // then
        // lineService.findLineById 메서드를 통해 검증
        Sections sections = 신분당선.getSections();
        assertThat(sections.getLineSections().size()).isEqualTo(2);
    }
}
