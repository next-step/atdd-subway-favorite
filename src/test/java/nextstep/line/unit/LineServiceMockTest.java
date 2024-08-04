package nextstep.line.unit;

import nextstep.line.domain.Line;
import nextstep.line.domain.LineRepository;
import nextstep.line.application.dto.LineResponse;
import nextstep.line.application.LineService;
import nextstep.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static nextstep.utils.UnitTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Mock을 활용한 지하철 노선 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class LineServiceMockTest {

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;

    @DisplayName("구간을 추가 함수는, 특정 노선에 구간을 추가하면 해당 구간이 추가된 노선 정보가 반환된다.")
    @Test
    void addSection() {
        // given
        LineService lineService = new LineService(lineRepository, stationRepository);

        when(stationRepository.findById(양재역.getId())).thenReturn(Optional.of(양재역));
        when(stationRepository.findById(교대역.getId())).thenReturn(Optional.of(교대역));
        Line 신분당선 = 신분당선(강남역, 양재역);
        when(lineRepository.findById(신분당선.getId())).thenReturn(Optional.of(신분당선));
        when(lineRepository.save(신분당선)).thenReturn(신분당선);

        // when
        LineResponse lineResponse = lineService.addSections(1L, createSectionRequest(양재역.getId(), 교대역.getId()));

        // then
        assertThat(lineResponse.getStations()).isEqualTo(createStationResponse(강남역, 양재역, 교대역));
    }
}
