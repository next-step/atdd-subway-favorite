package nextstep.subway.path.application;

import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.PathType;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GraphService {
    public PathResult findPath(List<LineResponse> lines, Long source, Long target, PathType type) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        // 지하철 역(정점)을 등록
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .map(it -> it.getStation())
                .distinct()
                .collect(Collectors.toList())
                .forEach(it -> graph.addVertex(it.getId()));

        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .filter(it -> it.getPreStationId() != null)
                .forEach(it -> graph.setEdgeWeight(graph.addEdge(it.getPreStationId(), it.getStation().getId()), type.findWeightOf(it)));

        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath result = dijkstraShortestPath.getPath(source, target);

        return new PathResult(result.getVertexList(), result.getWeight());
    }
}
