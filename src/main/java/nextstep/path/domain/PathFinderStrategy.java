package nextstep.path.domain;

import nextstep.line.domain.Line;
import nextstep.station.domain.Station;

import java.util.List;

public interface PathFinderStrategy {
    Path findShortestPathAndItsDistance(List<Line> lines, Station sourceStation, Station targetStation);
}
