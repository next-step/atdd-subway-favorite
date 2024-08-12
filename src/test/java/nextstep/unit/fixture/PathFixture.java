package nextstep.unit.fixture;

import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;

@SuppressWarnings("NonAsciiCharacters")
public class PathFixture {

    protected Long 교대역_id = 1L;
    protected Long 강남역_id = 2L;
    protected Long 양재역_id = 3L;
    protected Long 남부터미널역_id = 4L;

    protected Long 교대역_강남역_구간_id = 1L;
    protected Long 강남역_양재역_구간_id = 2L;
    protected Long 교대역_남부터미널_구간_id = 3L;
    protected Long 남부터미널_양재역_구간_id = 4L;

    protected Long 이호선_id = 1L;
    protected Long 신분당선_id = 2L;
    protected Long 삼호선_id = 3L;

    protected Station 교대역;
    protected Station 강남역;
    protected Station 양재역;
    protected Station 남부터미널역;

    protected Line 이호선;
    protected Line 신분당선;
    protected Line 삼호선;

    protected Section 교대역_강남역_구간;
    protected Section 강남역_양재역_구간;
    protected Section 교대역_남부터미널_구간;
    protected Section 남부터미널_양재역_구간;


    @BeforeEach
    void setUp() {
        교대역 = new Station(교대역_id, "교대역");
        강남역 = new Station(강남역_id, "강남역");
        양재역 = new Station(양재역_id, "양재역");
        남부터미널역 = new Station(남부터미널역_id, "남부터미널역");

        이호선 = new Line(이호선_id, "이호선", "green");
        신분당선 = new Line(신분당선_id, "신분당선", "red");
        삼호선 = new Line(삼호선_id, "삼호선", "blue");

        교대역_강남역_구간 = new Section(교대역_강남역_구간_id, 교대역, 강남역, 10, 이호선);
        강남역_양재역_구간 = new Section(강남역_양재역_구간_id, 강남역, 양재역, 10, 신분당선);
        교대역_남부터미널_구간 = new Section(교대역_남부터미널_구간_id, 교대역, 남부터미널역, 2, 삼호선);
        남부터미널_양재역_구간 = new Section(남부터미널_양재역_구간_id, 남부터미널역, 양재역, 3, 삼호선);

        이호선.addSection(교대역_강남역_구간);
        신분당선.addSection(강남역_양재역_구간);
        삼호선.addSection(교대역_남부터미널_구간);
        삼호선.addSection(남부터미널_양재역_구간);
    }
}
