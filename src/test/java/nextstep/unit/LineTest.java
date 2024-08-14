package nextstep.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.line.domain.Line;
import nextstep.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LineTest {

    private Line 신분당선;
    private Station 신사역;
    private Station 신논현역;
    private Station 논현역;
    private Station 강남역;

    @BeforeEach()
    void setUp() {
        신분당선 = new Line(1L, "신분당선", "bg-red-600");
        신사역 = new Station(1L, "신사역");
        신논현역 = new Station(2L, "신논현역");
        논현역 = new Station(3L, "논현역");
        강남역 = new Station(4L, "강남역");

        신분당선.addSection(신사역, 신논현역, 7);
        신분당선.addSection(신논현역, 논현역, 3);
        신분당선.addSection(논현역, 강남역, 10);
    }

    /**
     * Given: 노선에 구간이 3개가 존재하고,
     * When: 중간 역을 제거하면,
     * Then: 구간은 두개가 되고, 구간의 총길이는 변하지 않는다.
     */
    @DisplayName("중간 역을 삭제할 수 있다.")
    @Test
    void deleteSection_중간_역_삭제() {
        // given & when
        신분당선.deleteSection(신논현역);

        // then
        assertThat(신분당선.getSections().size()).isEqualTo(2);
        assertThat(신분당선.getDistance()).isEqualTo(20);
    }

    /**
     * Given: 노선에 구간이 3개가 존재하고,
     * When: 처음 역을 제거하면,
     * Then: 구간은 두개가 되고, 구간의 총길이는 변하지 않는다.
     */
    @DisplayName("처음 역을 삭제할 수 있다.")
    @Test
    void deleteSection_처음_역_삭제() {
        // given & when
        신분당선.deleteSection(신사역);

        // then
        assertThat(신분당선.getSections().size()).isEqualTo(2);
        assertThat(신분당선.getDistance()).isEqualTo(13);
    }

    /**
     * Given: 노선에 구간이 3개가 존재하고,
     * When: 마지막 역을 제거하면,
     * Then: 구간은 두개가 되고, 구간의 총길이는 변하지 않는다.
     */
    @DisplayName("마지막 역을 삭제할 수 있다.")
    @Test
    void deleteSection_마지막_역_삭제() {
        // given & when
        신분당선.deleteSection(강남역);

        // then
        assertThat(신분당선.getSections().size()).isEqualTo(2);
        assertThat(신분당선.getDistance()).isEqualTo(10);
    }
}
