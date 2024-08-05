package nextstep.subway.unit;

import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import nextstep.subway.domain.line.LineService;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.section.dto.SectionCreateRequest;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import nextstep.subway.domain.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nextstep.subway.utils.LineUtil.getStationNames;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class LineServiceTest {
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineService lineService;

    private Line 인천1호선;
    private Section 인천1호선_구간;
    private Station 계양역;
    private Station 국제업무지구역;
    private Station 인천터미널역;
    private Station 송도달빛축제공원역;
    private Station 신검암중앙역;

    @BeforeEach
    public void setUp() {
        계양역 = new Station("계양역");
        국제업무지구역 = new Station("국제업무지구역");
        인천터미널역 = new Station("인천터미널역");
        신검암중앙역 = new Station("신검암중앙역");
        송도달빛축제공원역 = new Station("송도달빛축제공원역");

        Arrays.asList(계양역, 국제업무지구역, 송도달빛축제공원역, 신검암중앙역, 인천터미널역)
                .forEach(station -> stationRepository.save(station));

        // 노선 저장
        인천1호선_구간 = new Section(계양역, 국제업무지구역, 15);
        인천1호선 = new Line("인천1호선", "bg-blue-400", 인천1호선_구간);
        인천1호선 = lineRepository.save(인천1호선);
    }

    @DisplayName("새 구간을 노선의 끝에 추가한다.")
    @Test
    void addSectionToLast() {
        //given
        // 새 구간 request dto 생성
        var 인천1호선_신구간 = new Section(국제업무지구역, 송도달빛축제공원역, 3);
        var 인천1호선_신구간_생성_요청 = SectionCreateRequest.of(인천1호선_신구간);

        // when
        // lineService.addSection 호출
        lineService.addSection(인천1호선.getId(), 인천1호선_신구간_생성_요청);

        // then
        // line.getSections 메서드를 통해 검증
        Line line = lineRepository.findById(인천1호선.getId()).get();
        assertThat(line.getSections().size()).isEqualTo(2);
        assertThat(getStationNames(line.getSections()))
                .containsExactly("계양역", "국제업무지구역", "송도달빛축제공원역");

    }

    @DisplayName("새 구간을 노선의 처음에 추가한다.")
    @Test
    void addSectionToFirst() {
        //given
        // 새 구간 request dto 생성
        var 인천1호선_신구간 = new Section(신검암중앙역, 계양역, 3);
        var 인천1호선_신구간_생성_요청 = SectionCreateRequest.of(인천1호선_신구간);

        // when
        // lineService.addSection 호출
        lineService.addSection(인천1호선.getId(), 인천1호선_신구간_생성_요청);

        // then
        // line.getSections 메서드를 통해 검증
        Line line = lineRepository.findById(인천1호선.getId()).get();
        assertThat(line.getSections().size()).isEqualTo(2);
        assertThat(getStationNames(line.getSections()))
                .containsExactly("신검암중앙역", "계양역", "국제업무지구역");
    }

    @DisplayName("기준역이 상행종점역인 새 구간을 노선의 가운데에 추가한다.")
    @Test
    void addSectionToMiddleBasedOnUpStation() {
        //given
        // 새 구간 request dto 생성
        var 인천1호선_신구간 = new Section(계양역, 인천터미널역, 6);
        var 인천1호선_신구간_생성_요청 = SectionCreateRequest.of(인천1호선_신구간);

        // when
        // lineService.addSection 호출
        lineService.addSection(인천1호선.getId(), 인천1호선_신구간_생성_요청);

        // then
        // line.getSections 메서드를 통해 검증
        Line line = lineRepository.findById(인천1호선.getId()).get();
        assertThat(line.getSections().size()).isEqualTo(2);
        assertThat(getStationNames(line.getSections()))
                .containsExactly("계양역", "인천터미널역", "국제업무지구역");
    }

    @DisplayName("기준역이 하행종점역인 새 구간을 노선의 가운데에 추가한다.")
    @Test
    void addSectionToMiddleBasedOnDownStation() {
        //given
        // 새 구간 request dto 생성
        var 인천1호선_신구간 = new Section(인천터미널역, 국제업무지구역, 6);
        var 인천1호선_신구간_생성_요청 = SectionCreateRequest.of(인천1호선_신구간);

        // when
        // lineService.addSection 호출
        lineService.addSection(인천1호선.getId(), 인천1호선_신구간_생성_요청);

        // then
        // line.getSections 메서드를 통해 검증
        Line line = lineRepository.findById(인천1호선.getId()).get();
        assertThat(line.getSections().size()).isEqualTo(2);
        assertThat(getStationNames(line.getSections()))
                .containsExactly("계양역", "인천터미널역", "국제업무지구역");
    }

    @DisplayName("지하철 구간 삭제")
    @Nested
    class deleteSection {
        @Test
        @DisplayName("첫번째 구간 삭제")
        void deleteFirstSection() {
            // given
            var 인천1호선_신구간 = new Section(신검암중앙역, 계양역, 4);
            var 인천1호선_신구간_생성_요청 = SectionCreateRequest.of(인천1호선_신구간);
            lineService.addSection(인천1호선.getId(), 인천1호선_신구간_생성_요청);

            //when
            lineService.deleteSection(인천1호선.getId(), 신검암중앙역.getId());

            //then
            var stations = lineService.findLineById(인천1호선.getId()).getStations();
            assertThat(stations.size()).isEqualTo(2);
            assertThat(getStationNames(stations)).containsExactly(계양역.getName(), 국제업무지구역.getName());
        }

        @Test
        @DisplayName("중간 구간 삭제")
        void deleteMiddleSection() {
            // given
            var 인천1호선_신구간 = new Section(계양역, 인천터미널역, 4);
            var 인천1호선_신구간_생성_요청 = SectionCreateRequest.of(인천1호선_신구간);
            lineService.addSection(인천1호선.getId(), 인천1호선_신구간_생성_요청);

            //when
            lineService.deleteSection(인천1호선.getId(), 인천터미널역.getId());

            //then
            var line = lineService.findLineById(인천1호선.getId());
            var stations = line.getStations();
            assertThat(stations.size()).isEqualTo(2);
            assertThat(getStationNames(stations)).containsExactly(계양역.getName(), 국제업무지구역.getName());
        }

        @Test
        @DisplayName("마지막 구간 삭제")
        void deleteLastSection() {
            // given
            var 인천1호선_신구간 = new Section(국제업무지구역, 송도달빛축제공원역, 4);
            var 인천1호선_신구간_생성_요청 = SectionCreateRequest.of(인천1호선_신구간);
            lineService.addSection(인천1호선.getId(), 인천1호선_신구간_생성_요청);

            //when
            lineService.deleteSection(인천1호선.getId(), 송도달빛축제공원역.getId());

            //then
            var line = lineService.findLineById(인천1호선.getId());
            var stations = line.getStations();
            assertThat(stations.size()).isEqualTo(2);
            assertThat(getStationNames(stations)).containsExactly(계양역.getName(), 국제업무지구역.getName());
        }
    }
}
