package nextstep.subway.unit;

import nextstep.subway.application.SectionService;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.infrastructure.LineRepository;
import nextstep.subway.infrastructure.SectionRepository;
import nextstep.subway.infrastructure.StationRepository;
import nextstep.subway.presentation.LineRequest;
import nextstep.subway.presentation.SectionRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SectionServiceMockTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private SectionRepository sectionRepository;

    private SectionService sectionService;

    @Test
    void addSection() {
        // given
        sectionService = new SectionService(lineRepository, sectionRepository, stationRepository);

        Long 신분당선_ID = 1L;
        Long 강남역_ID = 2L;
        Long 신논현역_ID = 3L;
        Long 신사역_ID = 4L;
        Station 강남역 = new Station("강남역");
        Station 신논현역 = new Station("신논현역");
        Station 신사역 = new Station("신사역");

        LineRequest lineRequest = new LineRequest(
                "신분당선",
                "bg-red-600",
                강남역_ID,
                신논현역_ID,
                10
        );
        Line 신분당선 = Line.createLine(강남역, 신논현역, lineRequest);

        when(lineRepository.findById(신분당선_ID)).thenReturn(Optional.of(신분당선));
        when(stationRepository.findById(신논현역_ID)).thenReturn(Optional.of(신논현역));
        when(stationRepository.findById(신사역_ID)).thenReturn(Optional.of(신사역));
        when(sectionRepository.save(any(Section.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        sectionService.addSection(신분당선_ID, new SectionRequest(신논현역_ID, 신사역_ID, 5));

        // then
        assertThat(신분당선.getSections().getStations().size()).isEqualTo(3);
    }
}
