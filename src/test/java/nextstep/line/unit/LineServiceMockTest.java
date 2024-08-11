package nextstep.line.unit;

import nextstep.line.domain.Line;
import nextstep.line.domain.LineRepository;
import nextstep.section.domain.Section;
import nextstep.station.application.StationService;
import nextstep.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class LineServiceMockTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationService stationService;



    @Test
    @DisplayName("지하철 구간을 생성한다.")
    void addSection() {
        // given
        // lineRepository, stationService stub 설정을 통해 초기값 셋팅
        Long 강남역_id = 1L;
        Long 양재역_id = 2L;
        Long 신분당선_id = 1L;

        Station 강남역 = new Station(강남역_id, "강남역");
        Station 양재역 = new Station(양재역_id, "양재역");
        Line 신분당선 = new Line(신분당선_id, "신분당선", "red");

        Mockito.lenient().when(lineRepository.findById(신분당선_id)).thenReturn(Optional.of(신분당선));
        Mockito.lenient().when(stationService.findById(강남역_id)).thenReturn(강남역);
        Mockito.lenient().when(stationService.findById(양재역_id)).thenReturn(양재역);

        // when
        // lineService.addSection 호출
        신분당선.addSection(new Section(강남역_id, 양재역_id, 10L));

        // then
        // line.findLineById 메서드를 통해 검증
        assertThat(신분당선.getStationIds()).containsAll(List.of(강남역_id, 양재역_id));
    }
}
