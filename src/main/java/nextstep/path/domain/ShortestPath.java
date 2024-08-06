package nextstep.path.domain;

import nextstep.line.domain.Section;
import nextstep.path.application.exception.NotAddedEndToPathsException;
import nextstep.path.application.exception.NotAddedStartToPathsException;
import nextstep.path.application.exception.NotConnectedPathsException;
import nextstep.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class ShortestPath {

    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private int maxDistance;

    private ShortestPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph, int maxDistance) {
        this.graph = graph;
        this.maxDistance = maxDistance;
    }

    public static ShortestPath from(List<Section> sections) {
        return new ShortestPath(createGraph(sections), maxDistance(sections));
    }

    private static WeightedMultigraph<Station, DefaultWeightedEdge> createGraph(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        sections.forEach(section -> {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        });
        return graph;
    }

    private static int maxDistance(List<Section> sections) {
        return sections.stream()
                .mapToInt(Section::getDistance)
                .max()
                .orElse(0);
    }

    public List<Station> getStations(Station start, Station end) {
        validateContains(start, end);
        GraphPath<Station, DefaultWeightedEdge> paths = findShortestPath(start, end);
        validateConnected(paths);
        return paths.getVertexList();
    }

    private void validateConnected(GraphPath<Station, DefaultWeightedEdge> paths) {
        if (paths == null) {
            throw new NotConnectedPathsException();
        }
    }

    public void validateConnected(Station start, Station end) {
        if (findShortestPath(start, end) == null) {
            throw new NotConnectedPathsException();
        }
    }

    public void validateContains(Station start, Station end) {
        if (!graph.containsVertex(start)) {
            throw new NotAddedStartToPathsException(start.getName());
        }
        if (!graph.containsVertex(end)) {
            throw new NotAddedEndToPathsException(end.getName());
        }
    }

    private GraphPath<Station, DefaultWeightedEdge> findShortestPath(Station start, Station end) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(start, end);
    }

    public int getDistance(Station start, Station end) {
        validateContains(start, end);
        List<GraphPath<Station, DefaultWeightedEdge>> paths = new KShortestPaths<>(graph, maxDistance).getPaths(start, end);
        validateConnected(paths);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = paths.get(0);
        return (int) shortestPath.getWeight();
    }

    private void validateConnected(List<GraphPath<Station, DefaultWeightedEdge>> paths) {
        if (paths.isEmpty()) {
            throw new NotConnectedPathsException();
        }
    }
}
