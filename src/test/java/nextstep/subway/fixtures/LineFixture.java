package nextstep.subway.fixtures;

import nextstep.subway.domain.entity.line.Line;
import nextstep.subway.domain.entity.line.LineSections;

import java.util.UUID;

public class LineFixture {
    public static Line prepareRandom(Long upStationId, Long downStationId) {
        Line line = new Line(UUID.randomUUID().toString(), UUID.randomUUID().toString(), new LineSections());
        line.addSection(upStationId, downStationId, 10L);
        return line;
    }

    public static Line prepareLineOne(Long... stationIds) {
        Line line = new Line("1호선", "#0052A4", new LineSections());

        for (long i = 0; i<stationIds.length-1; i++) {
            line.addSection(i, i+1, 10L);
        }
        return line;
    }
}
