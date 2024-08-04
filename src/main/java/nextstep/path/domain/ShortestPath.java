package nextstep.path.domain;

import nextstep.line.domain.Section;
import nextstep.path.application.exception.NotConnectedStationsException;
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
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(start, end);
        validateConnected(path);
        return path.getVertexList();
    }

    private static void validateConnected(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new NotConnectedStationsException();
        }
    }

    public int getDistance(Station start, Station end) {
        List<GraphPath<Station, DefaultWeightedEdge>> paths = new KShortestPaths<>(graph, maxDistance).getPaths(start, end);
        validateConnected(paths);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = paths.get(0);
        return (int) shortestPath.getWeight();
    }

    private static void validateConnected(List<GraphPath<Station, DefaultWeightedEdge>> paths) {
        if (paths.isEmpty()) {
            throw new NotConnectedStationsException();
        }
    }
}
