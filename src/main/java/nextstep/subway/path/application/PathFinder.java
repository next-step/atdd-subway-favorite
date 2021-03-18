package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathResult;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PathFinder {
    public PathResult findPath(List<Line> lines, Long source, Long target) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        // 지하철 역(정점)을 등록
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(it -> graph.addVertex(it.getId()));

        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> graph.setEdgeWeight(graph.addEdge(it.getUpStation().getId(), it.getDownStation().getId()), it.getDistance()));

        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath result = dijkstraShortestPath.getPath(source, target);

        return new PathResult(result.getVertexList(), result.getWeight());
    }
}
