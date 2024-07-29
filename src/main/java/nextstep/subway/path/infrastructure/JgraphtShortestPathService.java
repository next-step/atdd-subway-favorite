package nextstep.subway.path.infrastructure;

import lombok.RequiredArgsConstructor;
import nextstep.subway.common.ErrorMessage;
import nextstep.subway.exception.NotConnectedStationException;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.ShortestPathService;
import nextstep.subway.station.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JgraphtShortestPathService implements ShortestPathService {

    @Override
    public Path findShortestPath(List<Section> sections, Station sourceStation, Station targetStation) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = initializeDijkstra(sections);


        try {
            GraphPath<Station, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
            return new Path(shortestPath.getVertexList(), (long) shortestPath.getWeight());
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new NotConnectedStationException(ErrorMessage.NOT_CONNECTED_STATION);
        }
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> initializeDijkstra(List<Section> allSection) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        allSection.forEach(section -> {
            addStationToGraph(graph, section);
            setEdgeWeight(graph, section);
        });
        return new DijkstraShortestPath<>(graph);
    }

    private void addStationToGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section) {
        graph.addVertex(section.getUpStation());
        graph.addVertex(section.getDownStation());
    }

    private void setEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
    }
}
