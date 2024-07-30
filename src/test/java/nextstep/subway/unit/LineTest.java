package nextstep.subway.unit;

import nextstep.subway.line.SectionFixtures;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.StationFixtures;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LineTest {

    private Line line;

    @BeforeEach
    void setUp() {
        this.line = new Line("2호선", "green", SectionFixtures.FIRST_SECTION);
    }

    @Test
    void addSection() {
        // given
        // when
        line.addSection(SectionFixtures.ADD_FIRST_SECTION);

        // then
        Assertions.assertThat(line.getSections()).hasSize(2)
                .extracting("upStation", "downStation")
                .containsExactly(
                        Tuple.tuple(StationFixtures.FIRST_UP_STATION, StationFixtures.FIRST_DOWN_STATION),
                        Tuple.tuple(StationFixtures.FIRST_DOWN_STATION, StationFixtures.SECOND_UP_STATION)
                );
    }

    @Test
    void removeSection() {
        // given
        line.addSection(SectionFixtures.ADD_FIRST_SECTION);

        // when
        line.deleteSection(StationFixtures.SECOND_UP_STATION);

        // then
        Assertions.assertThat(line.getSections()).hasSize(1)
                .extracting("upStation", "downStation")
                .containsExactly(
                        Tuple.tuple(StationFixtures.FIRST_UP_STATION, StationFixtures.FIRST_DOWN_STATION)
                );
    }
}
