package nextstep.unit;

import nextstep.line.domain.Line;
import nextstep.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;

@SuppressWarnings("NonAsciiCharacters")
public class LineFixture {

    protected Line 신분당선;
    protected Station 신사역;
    protected Station 강남역;
    protected Station 논현역;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "bg-red-600");
        신사역 = new Station("신사역");
        강남역 = new Station("강남역");
        논현역 = new Station("논현역");
    }
}
