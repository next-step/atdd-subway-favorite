package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.common.InvalidSectionException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {
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
    @DisplayName("지하철 구간 연결 추가")
    void addSectionWithConnectedUpStation() {
        // given
        신분당선.addSection(새로운구간);
        Section 또다른구간 = Section.createSection(
                신분당선,
                신논현역,
                신사역,
                1
        );

        // when
        신분당선.addSection(또다른구간);
        Sections 신분당선구간 = 신분당선.getSections();

        // then
        assertThat(신분당선구간.getStations().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("지하철 구간 중간에 새로운 구간 추가")
    void addSectionInMiddle() {
        // given
        Sections 신분당선구간 = 신분당선.getSections();
        신분당선.addSection(새로운구간);

        Section 중간구간 = Section.createSection(
                신분당선,
                강남역,
                신사역,
                1
        );

        // when
        신분당선.addSection(중간구간);

        // then
        assertThat(신분당선구간.getStations().size()).isEqualTo(3);
        List<Station> stations = 신분당선구간.getStations();
        Assertions.assertThat(stations).containsExactlyInAnyOrder(강남역, 신사역, 신논현역);
    }

    @Test
    @DisplayName("지하철 연결되지 않은 구간 추가 시도")
    void addSectionWithNoConnectedStations() {
        // given
        신분당선.addSection(새로운구간);

        Section 연결되지않은구간 = Section.createSection(
                신분당선,
                판교역,
                정자역,
                1
        );

        // when & then
        assertThatThrownBy(() -> 신분당선.addSection(연결되지않은구간))
                .isInstanceOf(InvalidSectionException.class)
                .hasMessage("새로운 구간의 양쪽 역 모두 기존 노선에 연결되어 있지 않습니다.");
    }

    @Test
    @DisplayName("지하철 양쪽 역이 모두 연결된 구간 추가 시도")
    void addSectionWithBothConnectedStations() {
        // given
        신분당선.addSection(새로운구간);
        Section 또다른구간 = Section.createSection(
                신분당선,
                강남역,
                신논현역,
                1
        );

        // when & then
        assertThatThrownBy(() -> 신분당선.addSection(또다른구간))
                .isInstanceOf(InvalidSectionException.class)
                .hasMessage("새로운 구간이 기존 구간을 완전히 포함합니다.");
    }

    @Test
    @DisplayName("거리가 0인 구간 생성 불가")
    void addSectionWithZeroDistance() {

        // when & then
        assertThatThrownBy(() -> {
            Section.createSection(
                    신분당선,
                    강남역,
                    신논현역,
                    0
            );
        })
                .isInstanceOf(InvalidSectionException.class)
                .hasMessage("구간 거리는 0보다 커야 합니다.");
    }

    @Test
    @DisplayName("유효하지 않은 거리의 구간 추가 시도")
    void addSectionWithInvalidDistance() {
        // given
        신분당선.addSection(새로운구간);
        Section invalidSection = Section.createSection(
                신분당선,
                강남역,
                판교역,
                25
        );

        // when & then
        assertThatThrownBy(() -> {
            신분당선.addSection(invalidSection);
        })
                .isInstanceOf(InvalidSectionException.class)
                .hasMessage("기존 구간의 길이가 새 구간보다 길어야 합니다.");
    }

    @Test
    @DisplayName("지하철 구간 전체 역 조회")
    void getStations() {
        // given
        신분당선.addSection(새로운구간);
        Sections 신분당선구간 = 신분당선.getSections();

        // when
        List<Station> 신분당선전체역 = 신분당선구간.getStations();

        // then
        assertThat(신분당선전체역.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("지하철 구간 삭제")
    void removeSection() {
        // given
        Sections 신분당선구간 = 신분당선.getSections();
        신분당선.addSection(새로운구간);

        // when
        신분당선구간.deleteLastSection();

        // then
        assertThat(신분당선구간.getStations().isEmpty()).isTrue();
    }
}
