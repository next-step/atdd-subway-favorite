package nextstep.subway.domain;

import nextstep.subway.application.dto.PathResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathFinder {

    public PathResponse findPath(final List<Section> sections, final Station sourceStation, final Station targetStation) {
        if (sourceStation.isSame(targetStation)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "출발역과 도착역이 같습니다.");
        }

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = createGraph(sections);

        if (isNotContiansGraph(sourceStation, graph) || isNotContiansGraph(targetStation, graph)) {
            throw new IllegalArgumentException("그래프에 존재하지 않는 정점입니다.");
        }

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        final GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(sourceStation, targetStation);

        return new PathResponse(path.getVertexList(), path.getWeight());
    }

    private static boolean isNotContiansGraph(final Station sourceStation, final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        return !graph.containsVertex(sourceStation);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> createGraph(final List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        sections.forEach(section -> {
                    Station upStation = section.getUpStation();
                    Station downStation = section.getDownStation();
                    graph.addVertex(downStation);
                    graph.addVertex(upStation);
                    graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
                });

        return graph;
    }
}


