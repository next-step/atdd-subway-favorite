package nextstep.utils.fixture;

import nextstep.line.domain.Section;
import nextstep.line.ui.dto.section.SectionCreateRequestBody;
import nextstep.station.domain.Station;

public class SectionFixture {
    public static SectionCreateRequestBody 추가구간_생성_바디(Long upStationId, Long downStationId) {
        return new SectionCreateRequestBody(
                upStationId, downStationId, 10
        );
    }

    public static SectionCreateRequestBody 추가구간_생성_바디(Long upStationId, Long downStationId, int distance) {
        return new SectionCreateRequestBody(
                upStationId, downStationId, distance
        );
    }

    public static Section 추가구간_엔티티(Station upStation, Station downStation) {
        return Section.create(upStation, downStation, 10);
    }

    public static Section 추가구간_엔티티(Station upStation, Station downStation, int distance) {
        return Section.create(upStation, downStation, distance);
    }
}
