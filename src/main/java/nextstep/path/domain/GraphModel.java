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

    public Path findPath(List<Line> lines) {
        createGraphModel(lines);
        return findShortestPath(lines);
    }

    public void createGraphModel(List<Line> lines) {
        if(lines.isEmpty()) {
            throw new PathException(String.valueOf(PATH_NOT_FOUND));
        }

        for (Line line : lines) {
            addSectionsToGraph(line);
        }

        containsVertex(source);
        containsVertex(target);
    }

    private Path findShortestPath(List<Line> lines) {
        validateDuplicate(source, target);
        DijkstraShortestPath<Long, DefaultWeightedEdge> shortestPath =
                new DijkstraShortestPath<>(graph);
        GraphPath<Long, DefaultWeightedEdge> graphPath = shortestPath.getPath(source, target);

        if (graphPath.getVertexList() == null || graphPath.getVertexList().isEmpty()) {
            throw new PathException(String.valueOf(PATH_NOT_FOUND));
        }

        List<Station> stations = getStations(lines, graphPath.getVertexList());

        return new Path(stations, graphPath.getWeight());
    }

    public List<Station> getStations(List<Line> lines, List<Long> stationIds) {
        List<Station> stationList = new ArrayList<>();
        for (Long stationId : stationIds) {
            Station station = getStation(lines, stationId);
            stationList.add(station);
        }

        return stationList;
    }

    public Station getStation(List<Line> lines, Long stationId) {
        for (Line line : lines) {
            Optional<Station> foundStation = findStationInLine(line, stationId);
            if (foundStation.isPresent()) {
                return foundStation.get();
            }
        }
        throw new PathException(String.valueOf(PATH_NOT_FOUND));
    }

    private Optional<Station> findStationInLine(Line line, Long stationId) {
        List<Section> sectionList = line.getSections().getSections();
        for (Section section : sectionList) {
            if (section.getUpStation().getId().equals(stationId)) {
                return Optional.ofNullable(section.getUpStation());
            }
            if (section.getDownStation().getId().equals(stationId)) {
                return Optional.ofNullable(section.getDownStation());
            }
        }
        return Optional.empty();
    }

    public void addSectionsToGraph(Line line) {
        List<Section> sectionList = line.getSections().getSections();

        if (sectionList.isEmpty()) {
            throw new PathException(String.valueOf(PATH_NOT_FOUND));
        }
        for (Section section : sectionList) {
            addEdge(section.getUpStation().getId(), section.getDownStation().getId(), section.getDistance());
        }
    }

    public void addEdge(Long newSource, Long newTarget, double weight) {
        validateDuplicate(newSource, newTarget);
        graph.addVertex(newSource);
        graph.addVertex(newTarget);
        graph.setEdgeWeight(graph.addEdge(newSource, newTarget), weight);
    }

    public void containsVertex(Long vertexId) {
        if (!graph.containsVertex(vertexId)) {
            throw new PathException(String.valueOf(PATH_NOT_FOUND));
        }
    }

    public void validateDuplicate (Long source, Long target) {
        if(source.equals(target)) {
            throw new PathException(String.valueOf(PATH_DUPLICATE_STATION));
        }
    }

    public WeightedMultigraph<Long, DefaultWeightedEdge> getGraph() {
        return this.graph;
    }
}
