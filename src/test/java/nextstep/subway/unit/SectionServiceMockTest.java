package nextstep.subway.unit;

import nextstep.exception.BadRequestException;
import nextstep.subway.line.Line;
import nextstep.subway.line.LineRepository;
import nextstep.subway.line.LineService;
import nextstep.subway.section.Section;
import nextstep.subway.section.SectionRequest;
import nextstep.subway.section.SectionService;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SectionServiceMockTest {
    @InjectMocks
    SectionService sectionService;

    @InjectMocks
    LineService lineService;

    @Mock
    LineRepository lineRepository;

    @Mock
    StationRepository stationRepository;

    @Test
    @DisplayName("구간에 역을 추가한다.")
    void addSection() {
        // given
        Line 이호선 = new Line(1L, "2호선", "green");
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");

        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(선릉역.getId())).thenReturn(Optional.of(선릉역));

        when(lineRepository.findById(이호선.getId())).thenReturn(Optional.of(이호선));

        // when
        SectionRequest sectionRequest = new SectionRequest(강남역.getId(), 선릉역.getId(), 7);
        sectionService.addSection(이호선, sectionRequest);

        // then
        assertThat(lineService.findLineById(이호선.getId()).getStations()).hasSize(2);
    }

    @Test
    @DisplayName("구간 중간에 역을 추가한다.")
    void addMiddleSection() {
        //given
        Line 이호선 = new Line(1L, "2호선", "green");
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        Station 신규역 = new Station(3L, "역삼역");

        Section 강남_선릉_구간 = new Section(1L, 강남역, 선릉역, 10); //기존
        이호선.addSection(강남_선릉_구간);

        SectionRequest sectionRequest = new SectionRequest(강남역.getId(), 신규역.getId(), 3);

        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(신규역.getId())).thenReturn(Optional.of(신규역));

        when(lineRepository.findById(이호선.getId())).thenReturn(Optional.of(이호선));

        //when
        sectionService.addSection(이호선, sectionRequest);

        //then
        assertThat(lineService.findLineById(이호선.getId()).getStations()).hasSize(3);
    }

    @Test
    @DisplayName("신규 구간을 추가할 기존 구간을 찾을때 추가하려는 구간과 같은 경우 예외가 발생한다.")
    void sameSectionException() {
        Line 이호선 = new Line(1L, "이호선", "green");
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");

        Section 기존_구간 = new Section(1L, 강남역, 선릉역, 10);
        이호선.addSection(기존_구간);
        Section 등록할_구간 = new Section(2L, 강남역, 선릉역, 10);

        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(선릉역.getId())).thenReturn(Optional.of(선릉역));

        //when & then
        assertThatThrownBy(() -> sectionService.addSection(이호선, new SectionRequest(등록할_구간)))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("신규 구간을 추가할 기존 구간을 찾을 때 기존 구간 보다 신규 구간이 길면 예외가 발생한다.")
    void overDistanceSectionException() {
        Line 이호선 = new Line(1L, "이호선", "green");
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        Station 역삼역 = new Station(3L, "역삼역");

        Section 기존_구간 = new Section(1L, 강남역, 선릉역, 10);
        이호선.addSection(기존_구간);
        Section 등록할_구간 = new Section(2L, 강남역, 역삼역, 13);

        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(역삼역.getId())).thenReturn(Optional.of(역삼역));

        //when & then
        assertThatThrownBy(() -> sectionService.addSection(이호선, new SectionRequest(등록할_구간)))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("구간 맨 앞에 역을 추가한다.")
    void addFirstection() {
        //given
        Line 이호선 = new Line(1L, "2호선", "green");
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        Station 신규역 = new Station(3L, "서초역");

        Section 강남_선릉_구간 = new Section(1L, 강남역, 선릉역, 10); //기존
        이호선.addSection(강남_선릉_구간);

        SectionRequest sectionRequest = new SectionRequest(신규역.getId(), 강남역.getId(),3);

        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(신규역.getId())).thenReturn(Optional.of(신규역));

        when(lineRepository.findById(이호선.getId())).thenReturn(Optional.of(이호선));

        //when
        sectionService.addSection(이호선, sectionRequest);

        //then
        assertThat(lineService.findLineById(이호선.getId()).getStations()).hasSize(3);
    }

    @Test
    @DisplayName("2개의 구간이 존재하는 노선에서 가운데 역을 제거한다.")
    void deleteMiddleStation() {
        //given
        Line 이호선 = new Line(1L, "2호선", "green");
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        Station 삼성역 = new Station(3L, "삼성역");

        Section 강남_선릉_구간 = new Section(1L, 강남역, 선릉역, 10); //기존
        이호선.addSection(강남_선릉_구간);
        Section 선릉_삼성_구간 = new Section(2L, 선릉역, 삼성역, 5);
        이호선.addSection(선릉_삼성_구간);

        when(lineRepository.findById(이호선.getId())).thenReturn(Optional.of(이호선));

        sectionService.deleteSection(이호선, 선릉역);

        assertThat(lineService.findLineById(이호선.getId()).getStations()).hasSize(2);
    }
}
