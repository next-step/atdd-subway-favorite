package nextstep.subway.domain.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.domain.entity.line.Line;
import nextstep.subway.domain.entity.station.Station;

import java.util.List;

public interface PathFinder {
    PathResult find(List<Line> lines, Station source, Station target);

    @Getter
    @AllArgsConstructor
    class PathResult {
        private List<Long> stationIds;
        private Long distance;
    }
}
