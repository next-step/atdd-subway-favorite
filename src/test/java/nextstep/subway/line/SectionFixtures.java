package nextstep.subway.line;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.StationFixtures;

public class SectionFixtures {

    public static final Section FIRST_SECTION = Section.firstSection(StationFixtures.FIRST_UP_STATION, StationFixtures.FIRST_DOWN_STATION, 10L);
    public static final Section ADD_FIRST_SECTION = Section.firstSection(StationFixtures.FIRST_DOWN_STATION, StationFixtures.SECOND_UP_STATION, 20L);
    public static final Section SECOND_SECTION = Section.firstSection(StationFixtures.SECOND_UP_STATION, StationFixtures.SECOND_DOWN_STATION, 30L);

    public static final Section 논현_강남 = Section.firstSection(StationFixtures.논현역, StationFixtures.강남역, 4L);
    public static final Section 논현_고속 = Section.firstSection(StationFixtures.논현역, StationFixtures.고속터미널역, 2L);
    public static final Section 강남_양재 = Section.firstSection(StationFixtures.강남역, StationFixtures.양재역, 3L);
    public static final Section 고속_교대 = Section.firstSection(StationFixtures.고속터미널역, StationFixtures.교대역, 1L);
    public static final Section 교대_양재 = Section.firstSection(StationFixtures.교대역, StationFixtures.양재역, 3L);
}
