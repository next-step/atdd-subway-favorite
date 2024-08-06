package nextstep.path.domain;

import nextstep.line.entity.Line;
import nextstep.path.dto.Path;
import nextstep.path.exception.PathException;
import nextstep.section.entity.Section;
import nextstep.station.entity.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static nextstep.common.constant.ErrorCode.PATH_DUPLICATE_STATION;
import static nextstep.common.constant.ErrorCode.PATH_NOT_FOUND;

public class GraphModel {
    private WeightedMultigraph<Long, DefaultWeightedEdge> graph;
    private Long source;
    private Long target;

    public GraphModel(Long source, Long target) {
        validateDuplicate(source, target);
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        this.source = source;
        this.target = target;
    }

    public static GraphModel of(final Long source, final Long target) {
        return new GraphModel(source, target);
    }

    public Path findPath(final List<Line> lines) {
        createGraphModel(lines);
        return findShortestPath(lines);
    }

    public void createGraphModel(final List<Line> lines) {
        if (lines.isEmpty()) {
            throw new PathException(String.valueOf(PATH_NOT_FOUND));
        }

        for (Line line : lines) {
            addSectionsToGraph(line);
        }

        containsVertex(source);
        containsVertex(target);
    }

    private Path findShortestPath(final List<Line> lines) {
        validateDuplicate(source, target);
        DijkstraShortestPath<Long, DefaultWeightedEdge> shortestPath =
                new DijkstraShortestPath<>(graph);
        GraphPath<Long, DefaultWeightedEdge> graphPath = shortestPath.getPath(source, target);

        if (graphPath.getVertexList() == null || graphPath.getVertexList().isEmpty()) {
            throw new PathException(String.valueOf(PATH_NOT_FOUND));
        }

        List<Station> stations = getStations(lines, graphPath.getVertexList());

        return Path.of(stations, graphPath.getWeight());
    }

    public List<Station> getStations(final List<Line> lines, final List<Long> stationIds) {
        List<Station> stationList = new ArrayList<>();
        for (Long stationId : stationIds) {
            Station station = getStation(lines, stationId);
            stationList.add(station);
        }

        return stationList;
    }

    public Station getStation(final List<Line> lines, final Long stationId) {
        return findStationInLines(lines, stationId)
                .orElseThrow(() -> new PathException(String.valueOf(PATH_NOT_FOUND)));
    }

    private Optional<Station> findStationInLines(final List<Line> lines, final Long stationId) {
        return lines.stream()
                .map(line -> findStationInLine(line, stationId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private Optional<Station> findStationInLine(final Line line, final Long stationId) {
        List<Section> sectionList = line.getSections().getSections();
        return findStationInSections(sectionList, stationId);
    }

    private Optional<Station> findStationInSections(final List<Section> sections, final Long stationId) {
        return sections.stream()
                .map(section -> findStationInSection(section, stationId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private Optional<Station> findStationInSection(final Section section, final Long stationId) {
        if (section.getUpStation().getId().equals(stationId)) {
            return Optional.ofNullable(section.getUpStation());
        }
        if (section.getDownStation().getId().equals(stationId)) {
            return Optional.ofNullable(section.getDownStation());
        }
        return Optional.empty();
    }

    public void addSectionsToGraph(final Line line) {
        List<Section> sectionList = line.getSections().getSections();

        if (sectionList.isEmpty()) {
            throw new PathException(String.valueOf(PATH_NOT_FOUND));
        }
        for (Section section : sectionList) {
            addEdge(section.getUpStation().getId(), section.getDownStation().getId(), section.getDistance());
        }
    }

    public void addEdge(final Long newSource, final Long newTarget, double weight) {
        validateDuplicate(newSource, newTarget);
        graph.addVertex(newSource);
        graph.addVertex(newTarget);
        graph.setEdgeWeight(graph.addEdge(newSource, newTarget), weight);
    }

    public void containsVertex(final Long vertexId) {
        if (!graph.containsVertex(vertexId)) {
            throw new PathException(String.valueOf(PATH_NOT_FOUND));
        }
    }

    public void validateDuplicate(final Long source, final Long target) {
        if (source.equals(target)) {
            throw new PathException(String.valueOf(PATH_DUPLICATE_STATION));
        }
    }

    public WeightedMultigraph<Long, DefaultWeightedEdge> getGraph() {
        return this.graph;
    }
}

