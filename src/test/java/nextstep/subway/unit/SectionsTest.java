package nextstep.subway.unit;

import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.station.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철 노선 구간 일급 컬렉션 단위테스트")
public class SectionsTest {
    private Line 인천1호선;
    private Station 계양역;
    private Station 국제업무지구역;
    private Station 인천터미널역;
    private Station 송도달빛축제공원역;
    private Station 신검암중앙역;

    @BeforeEach
    void setup() {
        //given
        계양역 = new Station(1L, "계양역");
        국제업무지구역 = new Station(2L, "국제업무지구역");
        Section section = new Section(계양역, 국제업무지구역, 15);
        인천1호선 = new Line("인천1호선", "bg-blue-400", section);

        인천터미널역 = new Station(3L, "인천터미널역");
        신검암중앙역 = new Station(4L, "신검암중앙역");
        송도달빛축제공원역 = new Station(5L, "송도달빛축제공원역");
    }


    @DisplayName("새 구간을 노선의 끝에 추가한다.")
    @Test
    void successAddToEnd() {
        //when
        Section 신구간 = new Section(국제업무지구역, 송도달빛축제공원역, 3);

        인천1호선.addSection(신구간);

        //then
        assertThat(인천1호선.getSections()).last().isEqualTo(신구간);
    }

    @DisplayName("새 구간을 노선의 처음에 추가한다.")
    @Test
    void successAddToFirst() {
        //when
        Section 신구간 = new Section(신검암중앙역, 계양역, 3);

        인천1호선.addSection(신구간);

        //then
        assertThat(인천1호선.getSections()).first().isEqualTo(신구간);
    }

    @DisplayName("상행역이 동일한 새 구간을 노선의 가운데에 추가한다.")
    @Test
    void successAddToMiddleUpStaionCase() {
        //when
        Section 신구간 = new Section(계양역, 인천터미널역, 3);

        인천1호선.addSection(신구간);

        //then
        assertThat(인천1호선.getSections()).containsAnyOf(신구간);
    }

    @DisplayName("하행역이 동일한 새 구간을 노선의 가운데에 추가한다.")
    @Test
    void successAddToMiddleDownStationCase() {
        //when
        Section 신구간 = new Section(인천터미널역, 국제업무지구역, 3);

        인천1호선.addSection(신구간);

        //then
        assertThat(인천1호선.getSections()).containsAnyOf(신구간);
    }

    @DisplayName("노선 가운데에 추가되는 구간의 거리가 노선의 거리보다 짧지 않으면 구간 추가에 실패한다.")
    @Test
    void failAddWhenInvalidDistance() {
        //when
        Section 신구간 = new Section(계양역, 인천터미널역, 16);

        //then
        assertThatThrownBy(() -> 인천1호선.addSection(신구간)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("구간 등록 시 상행역과 하행역이 동일한 경우 구간 추가에 실패한다.")
    @Test
    void failAddWhenBothSameStation() {
        //when
        Section 동일_구간 = new Section(계양역, 국제업무지구역, 15);

        //then
        assertThatThrownBy(() -> 인천1호선.addSection(동일_구간)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("구간이 1개인 경우 역을 삭제할 수 없다.")
    @Test
    void deleteFailure() {

        //given
        Station deleteTarget = 국제업무지구역;
        assertThatThrownBy(() -> 인천1호선.deleteSection(deleteTarget)).isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("정상 구간을 제거한다.")
    @Test
    void successDelete() {
        //given
        인천1호선.addSection(new Section(국제업무지구역, 송도달빛축제공원역, 3));

        //when
        인천1호선.deleteSection(송도달빛축제공원역);

        //then
        assertThat(인천1호선.getSections().get(0)).isEqualTo(new Section(계양역, 국제업무지구역, 15));
    }
}
