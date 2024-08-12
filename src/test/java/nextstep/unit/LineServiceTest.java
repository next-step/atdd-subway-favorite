package nextstep.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.SubwayApplication;
import nextstep.global.exception.CustomException;
import nextstep.line.application.LineService;
import nextstep.line.domain.Sections;
import nextstep.line.infrastructure.LineRepository;
import nextstep.line.presentation.dto.SectionRequest;
import nextstep.station.infrastructure.StationRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(classes = SubwayApplication.class)
@Transactional
@Disabled
public class LineServiceTest extends LineFixture {
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private LineService lineService;

    @Test
    @DisplayName("노선에 구간을 가운데 지점에 추가 할 수 있다.")
    void addSection_노선에_가운데_구간_추가() {
        // given
        // stationRepository와 lineRepository를 활용하여 초기값 셋팅
        stationRepository.save(강남역);
        stationRepository.save(신사역);
        lineRepository.save(신분당선);
        lineService.addSection(신분당선.getId(), new SectionRequest(신사역.getId(), 강남역.getId(), 10));

        // when
        // lineService.addSection 호출
        stationRepository.save(논현역);
        lineService.addSection(신분당선.getId(), new SectionRequest(신사역.getId(), 논현역.getId(), 7));

        // then
        // line.getSections 메서드를 통해 검증
        Sections sections = 신분당선.getSections();
        assertThat(sections.size()).isEqualTo(2);
        assertThat(sections.getLineSections().get(0).getDistance()).isEqualTo(7);
    }

    @Test
    @DisplayName("노선에 구간을 처음 지점에 추가 할 수 있다.")
    void addSection_노선에_처음_구간_추가() {
        // given
        // stationRepository와 lineRepository를 활용하여 초기값 셋팅
        stationRepository.save(논현역);
        stationRepository.save(강남역);
        lineRepository.save(신분당선);
        lineService.addSection(신분당선.getId(), new SectionRequest(논현역.getId(), 강남역.getId(), 3));

        // when
        // lineService.addSection 호출
        stationRepository.save(신사역);
        lineService.addSection(신분당선.getId(), new SectionRequest(논현역.getId(), 신사역.getId(), 7));

        // then
        // line.getSections 메서드를 통해 검증
        Sections sections = 신분당선.getSections();

        assertThat(sections.getLineSections().size()).isEqualTo(2);
        assertThat(sections.calculateDistance()).isEqualTo(10);
    }

    @Test
    @DisplayName("노선에 똑같은 역은 추가할 수 없다")
    void addSection_노선_이미_등록된_역은_추가_할_수_없다() {
        // given
        stationRepository.save(논현역);
        stationRepository.save(강남역);
        lineRepository.save(신분당선);
        lineService.addSection(신분당선.getId(), new SectionRequest(논현역.getId(), 강남역.getId(), 3));

        // when & then
        stationRepository.save(강남역);
        assertThatThrownBy(() -> lineService.addSection(신분당선.getId(), new SectionRequest(논현역.getId(), 강남역.getId(), 3)))
                .isInstanceOf(CustomException.class);
    }
}

