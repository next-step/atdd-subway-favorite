package nextstep.subway.domain.entity;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

@Slf4j
public class PathFinder {

    private final List<Line> lines;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(List<Line> lines) {
        this.lines = lines;
        this.dijkstraShortestPath = new DijkstraShortestPath<>(createWeightedGraph());
    }

    public Optional<Path> findShortestPath(Station source, Station target) {
        if (source == target) {
            throw new IllegalArgumentException("Source and target stations are the same");
        }
        try {
            GraphPath<Station, DefaultWeightedEdge> result = dijkstraShortestPath.getPath(source, target);
            return Optional.of(new Path(result.getVertexList(), result.getWeight()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean isValidPath(Station source, Station target) {
        try {
            var path = findShortestPath(source, target);
            return path.isPresent();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private WeightedGraph<Station, DefaultWeightedEdge> createWeightedGraph() {
        SimpleWeightedGraph<Station, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        addStationsAsVerticesToGraph(graph);
        addSectionsAdSeightedEdgeToGraph(graph);
        return graph;
    }

    private void addStationsAsVerticesToGraph(Graph<Station, DefaultWeightedEdge> graph) {
        this.lines.stream()
            .flatMap(line -> line.getAllStationsByDistinct().stream())
            .forEach(graph::addVertex);
    }

    private void addSectionsAdSeightedEdgeToGraph(WeightedGraph<Station, DefaultWeightedEdge> graph) {
        this.lines.stream()
            .flatMap(line -> line.getSections().getAllSections().stream())
            .forEach(section -> graph.setEdgeWeight(
                graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance()));
    }
}
