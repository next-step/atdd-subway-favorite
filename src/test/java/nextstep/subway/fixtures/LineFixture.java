package nextstep.subway.fixtures;

import nextstep.subway.domain.entity.line.Line;
import nextstep.subway.domain.entity.line.LineSections;

import java.util.UUID;
import java.util.stream.IntStream;

public class LineFixture {
    public static Line prepareRandom(Long upStationId, Long downStationId) {
        Line line = new Line(UUID.randomUUID().toString(), UUID.randomUUID().toString(), new LineSections());
        line.addSection(upStationId, downStationId, 10L);
        return line;
    }

    public static Line prepareConnectedLine(Long... upDownStationIds) {
        Line line = new Line(UUID.randomUUID().toString(), UUID.randomUUID().toString(), new LineSections());
        IntStream
                .range(0, upDownStationIds.length-1)
                .forEach((i) -> line.addSection(upDownStationIds[i], upDownStationIds[i+1], 10L));
        return line;
    }
}
