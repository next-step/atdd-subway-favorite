package nextstep.subway.acceptance;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionsTest {

    private Station 양재역;
    private Station 강남역;
    private Station 고속터미널역;
    private Line 신분당선;
    private Sections sections;

    @BeforeEach
    void setUp() {
        sections = new Sections();

        양재역 = new Station("양재역");
        강남역 = new Station("강남역");
        고속터미널역 = new Station("고속터미널역");

        신분당선 = new Line("신분당선", "bg-red-600");
    }

    @Test
    @DisplayName("지하철 역이 경로에 포함되어 있으면 true를 반환한다")
    void contains_contain_returnTrue() {
        // given
        sections.add(new Section(신분당선, 강남역, 양재역, 10));
        sections.add(new Section(신분당선, 강남역, 고속터미널역, 5));

        // when
        boolean contains = sections.containsPath(양재역, 강남역);

        // then
        assertThat(contains).isTrue();
    }

    @Test
    @DisplayName("지하철 역이 경로에 포함되어 있지 않으면 false를 반환한다")
    void contains_notContain_returnFalse() {
        // given
        sections.add(new Section(신분당선, 강남역, 양재역, 10));

        // when
        boolean contains = sections.containsPath(양재역, 고속터미널역);

        // then
        assertThat(contains).isFalse();
    }
}
