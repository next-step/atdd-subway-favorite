package nextstep.subway.unit;

import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.station.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {
    private Line 인천1호선;
    private Station 계양역;
    private Station 국제업무지구역;

    @BeforeEach
    void setup() {
        //given
        계양역 = new Station(1L, "계양역");
        국제업무지구역 = new Station(2L, "국제업무지구역");
        Section section = new Section(계양역, 국제업무지구역, 15);
        인천1호선 = new Line("인천1호선", "bg-blue-400", section);
    }

    @DisplayName("구간을 추가한다.")
    @Test
    void addSection() {
        //when
        Station 송도달빛축제공원역 = new Station(3L, "송도달빛축제공원역");
        Section 인천1호선_신구간 = new Section(국제업무지구역, 송도달빛축제공원역, 3);

        인천1호선.addSection(인천1호선_신구간);

        //then
        assertThat(인천1호선.getSections()).containsAnyOf(인천1호선_신구간);
    }

    @DisplayName("노선의 모든 역을 조회한다.")
    @Test
    void getStations() {
        //given
        Station 송도달빛축제공원역 = new Station(3L, "송도달빛축제공원역");
        Section 인천1호선_신구간 = new Section(국제업무지구역, 송도달빛축제공원역, 3);
        인천1호선.addSection(인천1호선_신구간);

        //when
        var stations = 인천1호선.getStations();

        //then
        assertThat(stations.size()).isEqualTo(3);
        assertThat(stations).containsExactly(계양역, 국제업무지구역, 송도달빛축제공원역);
    }

    @DisplayName("노선의 마지막 구간을 삭제한다.")
    @Test
    void removeSection() {
        //when
        Station 송도달빛축제공원역 = new Station(3L, "송도달빛축제공원역");
        Section 인천1호선_신구간 = new Section(국제업무지구역, 송도달빛축제공원역, 3);
        인천1호선.addSection(인천1호선_신구간);

        인천1호선.deleteSection(송도달빛축제공원역);
        assertThat(인천1호선.getSections().get(0)).isEqualTo(new Section(계양역, 국제업무지구역, 15));
    }
}
