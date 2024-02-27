package nextstep.path.domain;

import nextstep.line.domain.Line;
import nextstep.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public interface PathFinderStrategy {
    Path findShortestPathAndItsDistance(List<Line> lines, Station sourceStation, Station targetStation);
}
