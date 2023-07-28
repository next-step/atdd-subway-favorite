package nextstep.subway.acceptance;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {

    private Station 강남역;
    private Station 양재역;
    private Station 고속터미널역;
    private Station 교대역;
    private Line 신분당선;

    @BeforeEach
    void setUp() {
        신분당선 = new Line();

        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        고속터미널역 = new Station("고속터미널역");
        교대역 = new Station("교대역");
    }

    @Test
    @DisplayName("지하철 역이 노선 경로에 포함되어 있는 경우 true를 반환한다")
    void containsSections_contain_returnTrue() {
        // given
        신분당선.addSection(강남역, 교대역, 10);
        신분당선.addSection(강남역, 고속터미널역, 5);
        신분당선.addSection(교대역, 양재역, 10);

        // when
        boolean contains = 신분당선.containsPath(강남역, 양재역);

        // then
        assertThat(contains).isTrue();
    }

    @Test
    @DisplayName("지하철 역이 노선 경로에 포함되지 않은 경우 false를 반환한다")
    void containsSections_notContain_returnFalse() {
        // given
        신분당선.addSection(강남역, 교대역, 10);
        신분당선.addSection(교대역, 양재역, 10);

        // when
        boolean contains = 신분당선.containsPath(강남역, 고속터미널역);

        // then
        assertThat(contains).isFalse();
    }
}
