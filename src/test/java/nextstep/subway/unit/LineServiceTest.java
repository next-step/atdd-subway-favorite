package nextstep.subway.unit;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.service.LineService;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationFixtures;
import nextstep.subway.station.StationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class LineServiceTest {
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineService lineService;

    @Test
    void addSection() {
        // given
        // stationRepository와 lineRepository를 활용하여 초기값 셋팅
        Station upStation = stationRepository.save(StationFixtures.FIRST_UP_STATION);
        Station downStation = stationRepository.save(StationFixtures.FIRST_DOWN_STATION);
        stationRepository.save(StationFixtures.SECOND_UP_STATION);
        Section section = Section.firstSection(upStation, downStation, 10L);
        Line line = new Line("2호선", "green", section);
        Line savedLine = lineRepository.save(line);

        // when
        // lineService.addSection 호출
        LineResponse lineResponse = lineService.addSection(savedLine.getId(), new SectionRequest(StationFixtures.FIRST_DOWN_STATION.getId(), StationFixtures.SECOND_UP_STATION.getId(), 20L));

        // then
        // line.getSections 메서드를 통해 검증
        Assertions.assertThat(line.getSections()).hasSize(2);
    }
}
