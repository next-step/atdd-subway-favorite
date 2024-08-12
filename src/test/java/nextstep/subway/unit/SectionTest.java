package nextstep.subway.unit;

import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.station.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 노선 구간 단위테스트")
public class SectionTest {

    private Line 인천1호선;
    private Station 계양역;
    private Station 국제업무지구역;
    private Station 인천터미널역;

    @BeforeEach
    void setup() {
        //given
        계양역 = new Station(1L, "계양역");
        국제업무지구역 = new Station(2L, "국제업무지구역");
        Section section = new Section(계양역, 국제업무지구역, 15);
        인천1호선 = new Line("인천1호선", "bg-blue-400", section);

        인천터미널역 = new Station(3L, "인천터미널역");
    }

    @DisplayName("지하철 구간을 업데이트한다.")
    @Test
    void updateSection() {
        Section 인천1호선_기존구간 = 인천1호선.getSections().get(0);
        인천1호선_기존구간.updateSection(계양역, 인천터미널역, 10);

        Assertions.assertThat(인천1호선_기존구간.getUpwardStation()).isEqualTo(계양역);
        Assertions.assertThat(인천1호선_기존구간.getDownwardStation()).isEqualTo(인천터미널역);
        Assertions.assertThat(인천1호선_기존구간.getDistance()).isEqualTo(10);
    }
}
