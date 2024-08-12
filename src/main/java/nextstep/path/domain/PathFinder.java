package nextstep.path.domain;

import static nextstep.global.exception.ExceptionCode.INVALID_CONNECT_PATH;
import static nextstep.global.exception.ExceptionCode.INVALID_DUPLICATE_PATH;

import java.util.List;
import java.util.Objects;
import nextstep.global.exception.CustomException;
import nextstep.line.domain.Section;
import nextstep.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class PathFinder {

    private ShortestPathAlgorithm<String, DefaultWeightedEdge> path;

    public List<String> getPath(List<Section> sections, Station source, Station target) {
        if (Objects.isNull(path)) {
            createPath(sections);
        }
        validatePath(path, source, target);
        return path.getPath(source.getName(), target.getName()).getVertexList();
    }

    public double getPathWeight(List<Section> sections, Station source, Station target) {
        if (Objects.isNull(path)) {
            createPath(sections);
        }
        validatePath(path, source, target);
        return path.getPath(source.getName(), target.getName()).getWeight();
    }

    private void createPath(List<Section> sections) {
        sections.forEach(section -> PathBuilder.addEdgeToGraph(section.getUpStationName(), section.getDownStationName(),
                section.getDistance()));
        path = PathBuilder.build();
    }

    private void validatePath(ShortestPathAlgorithm<String, DefaultWeightedEdge> path, Station source, Station target) {
        if (source.equals(target)) {
            throw new CustomException(INVALID_DUPLICATE_PATH);
        }

        GraphPath<String, DefaultWeightedEdge> pathPath = path.getPath(source.getName(), target.getName());
        if (Objects.isNull(pathPath)) {
            throw new CustomException(INVALID_CONNECT_PATH);
        }
    }

    private static class PathBuilder {
        private static final WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        public static ShortestPathAlgorithm<String, DefaultWeightedEdge> build() {
            return new DijkstraShortestPath<>(graph);
        }

        public static void addEdgeToGraph(String source, String target, int weight) {
            if (!graph.containsVertex(source)) {
                graph.addVertex(source);
            }

            if (!graph.containsVertex(target)) {
                graph.addVertex(target);
            }

            graph.setEdgeWeight(graph.addEdge(source, target), weight);
        }
    }
}
