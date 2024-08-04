package nextstep.path.application;

import nextstep.exceptions.ErrorMessage;
import nextstep.path.domain.WeightedEdge;
import nextstep.path.exceptions.PathNotFoundException;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class ShortestPathFinder<EDGE extends WeightedEdge<VERTEX>, VERTEX> {
    private final WeightedMultigraph<VERTEX, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath<VERTEX, DefaultWeightedEdge> shortestPath;

    public ShortestPathFinder() {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        shortestPath = new DijkstraShortestPath<>(graph);
    }

    public Optional<GraphPath<VERTEX, DefaultWeightedEdge>> find(final List<EDGE> edges, VERTEX source, VERTEX target) {
        addEdges(edges);
        setWeight(edges);
        try {
            return Optional.ofNullable(shortestPath.getPath(source, target));
        }catch (IllegalArgumentException exception) {
            throw new PathNotFoundException(ErrorMessage.PATH_NOT_FOUND);
        }
    }

    private void setWeight(final List<EDGE> edges) {
        edges.forEach(it -> {
            DefaultWeightedEdge edge = graph.addEdge(it.getSource(), it.getTarget());
            graph.setEdgeWeight(edge, it.getWeight());
        });
    }

    private void addEdges(final List<EDGE> edges) {
        edges.stream()
                .flatMap(it -> Stream.of(it.getTarget(), it.getSource()))
                .distinct()
                .forEach(graph::addVertex);
    }

}
