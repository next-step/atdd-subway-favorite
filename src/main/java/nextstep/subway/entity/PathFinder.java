package nextstep.subway.entity;

import nextstep.subway.dto.PathResponse;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public interface PathFinder {
    PathResponse getPath(WeightedMultigraph<Station, DefaultWeightedEdge> routeMap
            , Station source
            , Station target);
}
