package nextstep.subway.unit;

import nextstep.subway.domain.entity.Line;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.domain.request.SectionRequest;
import nextstep.subway.domain.response.LineResponse;
import nextstep.subway.domain.response.StationResponse;
import nextstep.subway.repository.LineRepository;
import nextstep.subway.service.LineService;
import nextstep.subway.service.StationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LineServiceMockTest {

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationService stationService;
    @InjectMocks
    private LineService lineService;

    private final Long STATION_ID_1 = 1L;
    private final Long STATION_ID_2 = 2L;
    private final Long LINE_ID_1 = 1L;

    @Test
    void addSection() {
        // given
        // lineRepository, stationService stub 설정을 통해 초기값 셋팅
        when(stationService.findById(STATION_ID_1))
                .thenReturn(new Station("강남역"));
        when(stationService.findById(STATION_ID_2))
                .thenReturn(new Station("역삼역"));

        when(lineRepository.findById(LINE_ID_1))
                .thenReturn(Optional.of(new Line("2호선", "green")));

        // when
        // lineService.addSection 호출
        lineService.addSection(LINE_ID_1, new SectionRequest(STATION_ID_1, STATION_ID_2, 10));

        // then
        // lineService.findLineById 메서드를 통해 검증
        LineResponse response = lineService.findLineById(LINE_ID_1);
        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.getStations()).hasSize(2),
                () -> assertThat(response.getStations().stream().map(StationResponse::getName)).contains("강남역", "역삼역")
        );
    }
}
