package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.common.InvalidSectionException;
import nextstep.subway.presentation.LineRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {
    private Station 강남역;
    private Station 신논현역;
    private Station 신사역;
    private Station 판교역;
    private Station 정자역;
    private Line 신분당선;
    private Section 새로운구간;

    @BeforeEach
    void setup() {
        강남역 = new Station("강남역");
        신논현역 = new Station("신논현역");
        신사역 = new Station("신사역");
        판교역 = new Station("판교역");
        정자역 = new Station("정자역");
        신분당선 = new Line("신분당선", "bg-red-600");
        새로운구간 = Section.createSection(
                신분당선,
                강남역,
                신논현역,
                3
        );
    }

    @Test
    @DisplayName("지하철 노선 생성")
    void createLineWithStations() {
        // given
        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 10L,9L,9);

        // when
        Line line = Line.createLine(강남역, 판교역, lineRequest);

        // then
        assertThat(line.getName()).isEqualTo("신분당선");
        assertThat(line.getColor()).isEqualTo("bg-red-600");
        assertThat(line.getSections().getStations()).containsExactly(강남역, 판교역);
    }

    @Test
    @DisplayName("지하철 노선 이름 변경")
    void changeName() {
        // given
        Line line = new Line("신분당선", "bg-red-600");
        String newName = "새로운 신분당선";

        // when
        line.changeName(newName);

        // then
        assertThat(line.getName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("지하철 노선 색상 변경")
    void changeColor() {
        // given
        Line line = new Line("신분당선", "bg-red-600");
        String newColor = "bg-blue-600";

        // when
        line.changeColor(newColor);

        // then
        assertThat(line.getColor()).isEqualTo(newColor);
    }

    @Test
    @DisplayName("지하철 노선에 구간 최초 추가")
    void addSectionToEmptySections() {
        // given
        Sections 신분당선구간 = 신분당선.getSections();

        // when
        신분당선.addSection(새로운구간);

        // then
        assertThat(신분당선구간.getStations().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("지하철 노선의 중간역 삭제")
    void deleteMiddleStation() {
        // given
        신분당선.addSection(새로운구간);
        신분당선.addSection(Section.createSection(신분당선, 신논현역, 신사역, 2));

        // when
        신분당선.deleteStation(신논현역);

        // then
        Sections 신분당선구간 = 신분당선.getSections();
        assertThat(신분당선구간.getStations().size()).isEqualTo(2);
        assertThat(신분당선구간.getStations()).containsExactly(강남역, 신사역);
    }

    @Test
    @DisplayName("지하철 노선의 첫 역 삭제")
    void deleteFirstStation() {
        // given
        신분당선.addSection(새로운구간);
        신분당선.addSection(Section.createSection(신분당선, 신논현역, 신사역, 2));

        // when
        신분당선.deleteStation(강남역);

        // then
        Sections 신분당선구간 = 신분당선.getSections();
        assertThat(신분당선구간.getStations().size()).isEqualTo(2);
        assertThat(신분당선구간.getStations()).containsExactly(신논현역, 신사역);
    }

    @Test
    @DisplayName("지하철 노선의 마지막 역 삭제")
    void deleteLastStation() {
        // given
        신분당선.addSection(새로운구간);
        신분당선.addSection(Section.createSection(신분당선, 신논현역, 신사역, 2));

        // when
        신분당선.deleteStation(신사역);

        // then
        Sections 신분당선구간 = 신분당선.getSections();
        assertThat(신분당선구간.getStations().size()).isEqualTo(2);
        assertThat(신분당선구간.getStations()).containsExactly(강남역, 신논현역);
    }

    @Test
    @DisplayName("지하철 노선에 없는 역 삭제 시도")
    void deleteNonExistentStation() {
        // given
        신분당선.addSection(새로운구간);

        // when & then
        assertThatThrownBy(() -> 신분당선.deleteStation(판교역))
                .isInstanceOf(InvalidSectionException.class)
                .hasMessage("노선에 포함되지 않은 역을 제거할 수 없습니다.");
    }
}
