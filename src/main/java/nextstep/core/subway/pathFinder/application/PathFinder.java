package nextstep.core.subway.pathFinder.application;

import nextstep.core.subway.line.domain.Line;
import nextstep.core.subway.pathFinder.domain.dto.PathFinderResult;
import nextstep.core.subway.section.domain.Section;
import nextstep.core.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PathFinder {
    public PathFinderResult calculateShortestPath(List<Line> lines, Station departureStation, Station arrivalStation) {
        GraphPath<Station, DefaultWeightedEdge> path = findShortestPath(lines, departureStation, arrivalStation);

        if (!hasFoundPath(path)) {
            throw new IllegalArgumentException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
        return generatePathResult(path);
    }

    public boolean existPathBetweenStations(List<Line> lines, Station departureStation, Station arrivalStation) {
        return hasFoundPath(findShortestPath(lines, departureStation, arrivalStation));
    }

    private GraphPath<Station, DefaultWeightedEdge> findShortestPath(List<Line> lines, Station departureStation, Station arrivalStation) {
        validateLines(lines, departureStation, arrivalStation);

        return findShortestPath(
                departureStation,
                arrivalStation,
                buildPathFromLines(lines, new WeightedMultigraph<Station, DefaultWeightedEdge>(DefaultWeightedEdge.class)));
    }

    private void validateLines(List<Line> lines, Station departureStation, Station arrivalStation) {
        if (!hasAtLeastOneSection(lines)) {
            throw new IllegalArgumentException("노선에 연결된 구간이 하나라도 존재해야 합니다.");
        }

        if (doesNotContainStation(lines, departureStation)) {
            throw new IllegalArgumentException("노선에 연결된 출발역이 아닙니다.");
        }

        if (doesNotContainStation(lines, arrivalStation)) {
            throw new IllegalArgumentException("노선에 연결된 도착역이 아닙니다.");
        }
    }

    private boolean doesNotContainStation(List<Line> lines, Station station) {
        return lines.stream()
                .noneMatch(line -> line.getAllStations().contains(station));
    }

    private boolean hasAtLeastOneSection(List<Line> lines) {
        return getTotalSectionCount(lines) > 0;
    }

    private int getTotalSectionCount(List<Line> lines) {
        return lines.stream()
                .mapToInt(line -> line.getSortedAllSections().size())
                .sum();
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> buildPathFromLines(List<Line> lines,
                                                                                WeightedMultigraph<Station, DefaultWeightedEdge> path) {
        lines.forEach(line -> buildPathFromSections(path, line.getSortedAllSections()));
        return path;
    }

    private void buildPathFromSections(WeightedMultigraph<Station, DefaultWeightedEdge> path, List<Section> sections) {
        sections.forEach(section -> {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();

            path.addVertex(upStation);
            path.addVertex(downStation);
            path.setEdgeWeight(path.addEdge(upStation, downStation), section.getDistance());
        });
    }

    private boolean hasFoundPath(GraphPath<Station, DefaultWeightedEdge> path) {
        return path != null;
    }

    private PathFinderResult generatePathResult(GraphPath<Station, DefaultWeightedEdge> path) {
        return new PathFinderResult(path.getVertexList(), (int) path.getWeight());
    }

    private GraphPath<Station, DefaultWeightedEdge> findShortestPath(Station departureStation,
                                                                     Station arrivalStation,
                                                                     WeightedMultigraph<Station, DefaultWeightedEdge> path) {
        return new DijkstraShortestPath<>(path).getPath(departureStation, arrivalStation);
    }
}
