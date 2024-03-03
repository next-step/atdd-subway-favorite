package nextstep.path;

import nextstep.section.Section;
import nextstep.station.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Path {
    private final List<Station> stations;
    private final long distance;

    public Path(List<Section> sections, Station start, Station end) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        init(graph, sections);

        validateIsConnected(graph, start, end);
        validateIsDifferentStation(start, end);

        GraphPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph).getPath(start, end);

        this.stations = path.getVertexList();
        this.distance = (long) path.getWeight();
    }

    private void init(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
        for (Station station : toStations(sections)) {
            if (!graph.containsVertex(station)) {
                graph.addVertex(station);
            }
        }
        for (Section section : sections) {
            if (!graph.containsEdge(section.getUpStation(), section.getDownStation())) {
                graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
            }
        }
    }

    private List<Station> toStations(List<Section> sections) {
        List<Station> upStations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        List<Station> downStations = sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
        return Stream.concat(upStations.stream(), downStations.stream())
                .distinct()
                .collect(Collectors.toList());
    }

    private void validateIsConnected(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Station start, Station end) {
        if (!graph.containsVertex(start) || !graph.containsVertex(end)) {
            throw new IllegalArgumentException("출발역 또는 도착역이 연결되어 있지 않습니다");
        }
    }

    private void validateIsDifferentStation(Station start, Station end) {
        if (start.equals(end)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    public List<Station> getStations() {
        return stations;
    }

    public long getDistance() {
        return distance;
    }
}
