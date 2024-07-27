package nextstep.subway.infrastructure.jgrapht;

import nextstep.subway.domain.entity.line.Line;
import nextstep.subway.domain.entity.line.LineSections;
import nextstep.subway.domain.entity.station.Station;
import nextstep.subway.domain.exception.SubwayDomainException;
import nextstep.subway.domain.exception.SubwayDomainExceptionType;
import nextstep.subway.domain.query.PathFinder;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JgraphtPathFinder implements PathFinder {
    @Override
    public PathResult find(List<Line> lines, Station source, Station target) {
        verifySameStation(source, target);

        WeightedMultigraph<Long, DefaultWeightedEdge> graph = initGraph(lines, source, target);

        GraphPath<Long, DefaultWeightedEdge> shortestPath = findShortestPath(graph, source, target);

        return new PathResult(shortestPath.getVertexList(), (long) shortestPath.getWeight());
    }

    private WeightedMultigraph<Long, DefaultWeightedEdge> initGraph(List<Line> lines, Station source, Station target) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        lines.forEach((line) -> addEdges(graph, line.getSections()));
        verifyStationExistedInGraph(graph, source, target);
        return graph;
    }

    private void addEdges(WeightedMultigraph<Long, DefaultWeightedEdge> graph, LineSections sections) {
        sections.forEach((section -> {
            graph.addVertex(section.getUpStationId());
            graph.addVertex(section.getDownStationId());
            graph.setEdgeWeight(graph.addEdge(section.getUpStationId(), section.getDownStationId()), section.getDistance());
        }));
    }

    private static GraphPath<Long, DefaultWeightedEdge> findShortestPath(
            WeightedMultigraph<Long, DefaultWeightedEdge> graph,
            Station source,
            Station target
    ) {
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraPath = new DijkstraShortestPath<>(graph);
        GraphPath<Long, DefaultWeightedEdge> path = dijkstraPath.getPath(source.getId(), target.getId());
        if (path == null) {
            throw new SubwayDomainException(SubwayDomainExceptionType.SOURCE_TARGET_NOT_CONNECTED);
        }
        return path;
    }

    private void verifySameStation(Station source, Station target) {
        if (source.isSameStation(target)) {
            throw new SubwayDomainException(SubwayDomainExceptionType.SOURCE_TARGET_SAME_STATION);
        }
    }

    private void verifyStationExistedInGraph(WeightedMultigraph<Long, DefaultWeightedEdge> graph, Station source, Station target) {
        if (!graph.containsVertex(source.getId())) {
            throw new SubwayDomainException(SubwayDomainExceptionType.NOT_FOUND_STATION);
        }

        if (!graph.containsVertex(target.getId())) {
            throw new SubwayDomainException(SubwayDomainExceptionType.NOT_FOUND_STATION);
        }
    }
}
