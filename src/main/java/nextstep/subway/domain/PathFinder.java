package nextstep.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final WeightedMultigraph<Long, DefaultWeightedEdge> graph;

    public PathFinder(SectionEdges edges) {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        edges.getEdges().forEach(edge -> {
            Long source = edge.getSource();
            Long target = edge.getTarget();
            graph.addVertex(source);
            graph.addVertex(target);
            int weight = edge.getWeight();
            graph.setEdgeWeight(graph.addEdge(source, target), weight);
        });
    }

    public Path findShortedPath(Long source, Long target) {
        return Path.shortestPath(graph, source, target);
    }
}
