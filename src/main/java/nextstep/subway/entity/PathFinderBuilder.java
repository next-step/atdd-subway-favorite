package nextstep.subway.entity;

import nextstep.subway.dto.PathResponse;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinderBuilder {
    private PathFinder pathFinder;
    private WeightedMultigraph<Station, DefaultWeightedEdge> routeMap;
    private Station source;
    private Station target;

    public PathFinderBuilder(PathFinder pathFinder) {
        this.pathFinder = pathFinder;
        this.routeMap = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    public PathFinderBuilder addVertex(List<Station> stations) {
        stations.stream().forEach(this.routeMap::addVertex);
        return this;
    }

    public PathFinderBuilder addEdgeWeight(List<Section> sections) {
        sections.stream()
                .forEach(s -> this.routeMap.setEdgeWeight(
                        this.routeMap.addEdge(s.getUpStation(), s.getDownStation())
                        , s.getDistance()
                ));
        return this;
    }

    public PathFinderBuilder setSource(Station station) {
        this.source=station;
        return this;
    }

    public PathFinderBuilder setTarget(Station station) {
        this.target=station;
        return this;
    }

    public PathResponse find() {
        return this.pathFinder.getPath(this.routeMap, this.source, this.target);
    }
}
