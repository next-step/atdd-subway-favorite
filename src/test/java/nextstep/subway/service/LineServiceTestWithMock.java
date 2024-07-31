package nextstep.subway.service;

import nextstep.subway.dto.LineResponse;
import nextstep.subway.dto.SectionRequest;
import nextstep.subway.entity.Line;
import nextstep.subway.entity.Section;
import nextstep.subway.entity.Sections;
import nextstep.subway.entity.Station;
import nextstep.subway.repository.LineRepository;
import nextstep.subway.repository.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LineServiceTestWithMock {
    private final Station 신사역 = new Station(1L, "신사역");
    private final Station 강남역 = new Station(2L, "강남역");
    private final Station 판교역 = new Station(3L, "판교역");
    private Line 신분당선;
    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private LineService lineService;

    @BeforeEach
    void setUp() {
        신분당선 = new Line(1L, "신분당선", "#77777");
        when(lineRepository.findById(신분당선.getId())).thenReturn(Optional.of(신분당선));
        when(stationRepository.findById(신사역.getId())).thenReturn(Optional.of(신사역));
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
    }

    @Test
    @DisplayName("노선 구간 추가")
    void addSection() {
        // Given
        SectionRequest 신사_강남_구간_추가_요청 = new SectionRequest(신사역.getId(), 강남역.getId(), 10L);

        // When
        LineResponse response = lineService.addSection(신분당선.getId(), 신사_강남_구간_추가_요청);

        // Then
        assertThat(response.getStations()).hasSize(2);
        assertThat(response.getStations().get(0).getId()).isEqualTo(신사역.getId());
        assertThat(response.getStations().get(1).getId()).isEqualTo(강남역.getId());

    }

    @Test
    @DisplayName("노선 구간 제거")
    void removeSection() {
        //given
        when(stationRepository.findById(판교역.getId())).thenReturn(Optional.of(판교역));
        SectionRequest 신사_강남_구간_추가_요청 = new SectionRequest(신사역.getId(), 강남역.getId(), 10L);
        SectionRequest 강남_판교_구간_추가_요청 = new SectionRequest(강남역.getId(), 판교역.getId(), 10L);
        lineService.addSection(신분당선.getId(), 신사_강남_구간_추가_요청);
        lineService.addSection(신분당선.getId(), 강남_판교_구간_추가_요청);

        //when
        lineService.removeSection(신분당선.getId(), 판교역.getId());

        //then
        Sections sections = 신분당선.getSections();
        assertThat(sections.getSectionList()).hasSize(1);

        Section section = sections.getSectionList().get(0);
        assertThat(section.getUpStation()).isEqualTo(신사역);
        assertThat(section.getDownStation()).isEqualTo(강남역);
    }
}