package nextstep.subway.unit;

import nextstep.subway.line.Line;
import nextstep.subway.line.LineRepository;
import nextstep.subway.line.LineService;
import nextstep.subway.section.Section;
import nextstep.subway.section.SectionRequest;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;
import nextstep.subway.station.StationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class LineServiceMockTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private LineService lineService;

    @Test
    void addSection() {
        // given
        LineService lineService = mock(LineService.class);

        Line 이호선 = new Line(1L, "2호선", "green");
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");

        // when
        SectionRequest sectionRequest = new SectionRequest(강남역.getId(), 선릉역.getId(), 7);
        lineService.addSection(이호선.getId(), sectionRequest);

        // then
        verify(lineService).addSection(이호선.getId(), sectionRequest);
    }

    @Test
    void deleteSection() {
        // given
        Line 이호선 = new Line(1L, "2호선", "green");
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        Station 삼성역 = new Station(3L, "삼성역");

        when(lineRepository.findById(이호선.getId())).thenReturn(Optional.of(이호선));
        when(stationRepository.findById(삼성역.getId())).thenReturn(Optional.of(삼성역));

        Section 강남_선릉_구간 = new Section(1L, 강남역, 선릉역,7);
        Section 선릉_삼성_구간 = new Section(2L, 선릉역, 삼성역, 3);

        이호선.getSectionList().add(강남_선릉_구간);
        이호선.getSectionList().add(선릉_삼성_구간);

        //when
        lineService.deleteSection(이호선.getId(), 삼성역.getId());

        //then
        assertAll(
                () -> assertThat(lineService.findLineById(이호선.getId()).getStations()).hasSize(2),
                () ->assertThat(lineService.findLineById(이호선.getId()).getStations()).containsAll(List.of(new StationResponse(강남역), new StationResponse(선릉역)))
        );
    }
}
