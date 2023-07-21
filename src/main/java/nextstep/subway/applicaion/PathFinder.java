package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.NotConnectedPathException;
import nextstep.subway.exception.SameOriginPathException;
import nextstep.subway.exception.StationNotFoundException;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathFinder {

    public static PathResponse find(Long sourceId, Long targetId, List<Section> sections) {
        validate(sourceId, targetId);
        Set<Station> stations = getStationSet(sections);
        Station sourceStation = findStation(sourceId, stations);
        Station targetStation = findStation(targetId, stations);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = createWeightedMultigraph(sections, stations);

        List<Station> shortestPath = getShortestPath(graph, sourceStation, targetStation);
        double weight = getWeight(graph, sourceStation, targetStation);

        return new PathResponse(shortestPath, (int) weight);
    }

    private static void validate(Long sourceId, Long targetId) {
        if (sourceId.equals(targetId)) {
            throw new SameOriginPathException();
        }
    }

    private static double getWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                                    Station sourceStation,
                                    Station targetStation) {

        try {
            DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
            return dijkstraShortestPath.getPathWeight(sourceStation, targetStation);
        } catch (NullPointerException e) {
            throw new NotConnectedPathException();
        }
    }

    private static List<Station> getShortestPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                                                 Station sourceStation,
                                                 Station targetStation) {

        try {
            DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
            return dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList();
        } catch (NullPointerException e) {
            throw new NotConnectedPathException();
        }
    }

    private static WeightedMultigraph<Station, DefaultWeightedEdge> createWeightedMultigraph(List<Section> sections,
                                                                                             Set<Station> stations) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        stations.forEach(graph::addVertex);
        sections.forEach(section ->
                graph.setEdgeWeight(
                        graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance()
                )
        );
        return graph;
    }

    private static Set<Station> getStationSet(List<Section> sections) {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .collect(Collectors.toSet());
    }

    private static Station findStation(Long sourceId, Set<Station> stations) {
        return stations.stream().filter(station -> station.hasId(sourceId)).findAny()
                .orElseThrow(StationNotFoundException::new);
    }
}
