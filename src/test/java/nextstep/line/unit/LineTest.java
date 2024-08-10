package nextstep.line.unit;

import nextstep.line.domain.Line;
import nextstep.section.domain.Section;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    private static Long 강남역;
    private static Long 양재역;
    private static Long 판교역;
    private static Long 광교역;
    private static Line 신분당선;

    @BeforeEach
    void initStation() {
        강남역 = 1L;
        양재역 = 2L;
        판교역 = 3L;
        광교역 = 4L;
        신분당선 = new Line("신분당선", "bg-red-600");
        신분당선.addSection(new Section(신분당선, 강남역, 양재역, 10L));

    }

    @DisplayName("지하철 구간 추가 테스트")
    @Nested
    class AddSectionTest {
        @DisplayName("기존 하행역을 상행역으로 하는 구간을 추가한다")
        @Test
        void addSection() {
            // given
            // 기존 하행역을 상행역으로 하는 구간을 생성한다.
            Section section = new Section(신분당선, 양재역, 판교역, 10L);

            // when
            // 해당 구간을 노선에 추가한다.
            신분당선.addSection(section);


            // then
            // 추가한 노선에 추가한 구간의 하행역이 포함된다.
            assertThat(신분당선.getStationIds().equals(판교역));


        }
    }

    @DisplayName("지하철 조회 테스트")
    @Nested
    class GetStationTest {
        @DisplayName("지하철 역을 조회한다.")
        @Test
        void getStations() {

            // then
            // 노선에 포함된 역 조회시, 기존에 추가된 노선인 강남역, 양재역이 모두 포함되어있다.
            assertThat(신분당선.getStationIds().containsAll(List.of(강남역, 양재역)));

        }
    }

    @DisplayName("지하철 구간 삭제 테스트")
    @Nested
    class removeSectionTest {
        @DisplayName("노선에 포함된 구간을 삭제한다.")
        @Test
        void removeSection() {
            // given
            // 노선에는 3개의 역이 포함되어있다.
            Section section = new Section(신분당선, 양재역, 판교역, 10L);
            신분당선.addSection(section);


            // when
            // 마지막 구간(역)을 삭제한다.
            신분당선.removeStation(판교역);

            // then
            // 노선에 포함된 역 조회시, 마지막 구간(역)이 조회되지 않는다.
            assertThat(신분당선.getStationIds()).doesNotContain(판교역);
        }
    }

}
