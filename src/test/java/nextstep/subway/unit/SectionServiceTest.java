package nextstep.subway.unit;

import nextstep.subway.application.SectionService;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.infrastructure.LineRepository;
import nextstep.subway.infrastructure.StationRepository;
import nextstep.subway.presentation.SectionRequest;
import nextstep.subway.application.SectionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class SectionServiceTest {
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private SectionService sectionService;

    @Test
    void addSection() {
        // given
        Line 신분당선 = lineRepository.save(new Line("신분당선", "bg-red-600"));
        Station 강남역 = stationRepository.save(new Station("강남역"));
        Station 신논현역 = stationRepository.save(new Station("신논현역"));

        // when
        SectionResponse sectionResponse
                = sectionService.addSection(
                신분당선.getId(),
                new SectionRequest(강남역.getId(), 신논현역.getId(), 5)
        );

        // then
        Line findLine = lineRepository.findById(sectionResponse.getLineId()).orElseThrow(null);
        Sections sections = findLine.getSections();
        assertThat(sections.getStations().size()).isEqualTo(2);
    }
}
