package nextstep.subway.acceptance;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {

    @Test
    @DisplayName("노선 구간에 포함되어 있는 역인지 확인")
    void containsSections() {
        Line line = new Line();
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");

        line.addSection(강남역, new Station("교대역"), 10);
        line.addSection(new Station("강남역"), new Station("고속터미널역"), 5);
        line.addSection(new Station("교대역"), 양재역, 10);

        assertThat(line.containsSection(강남역)).isTrue();
    }
}
