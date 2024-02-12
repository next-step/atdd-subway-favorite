package nextstep.path.domain;

import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.path.exception.PathNotFoundException;
import nextstep.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Optional;

public class SubwayMap {
    private final List<Line> lines;

    public SubwayMap(final List<Line> lines) {
        this.lines = lines;
    }

    public Optional<Path> findShortestPath(final Station sourceStation, final Station targetStation) {
        final DijkstraShortestPath<Station, DefaultWeightedEdge> path = getShortestPath(sourceStation, targetStation);

        return Optional.ofNullable(path.getPath(sourceStation, targetStation))
                .map(shortestPath -> new Path(shortestPath.getVertexList(), (int) shortestPath.getWeight()));
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> getShortestPath(final Station sourceStation, final Station targetStation) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = buildGraph();

        if (!(graph.containsVertex(sourceStation) && graph.containsVertex(targetStation))) {
            throw new PathNotFoundException();
        }

        return new DijkstraShortestPath<>(graph);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> buildGraph() {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        lines.stream()
                .flatMap(line -> line.getSections().stream())
                .forEach(section -> initGraph(section, graph));
        return graph;
    }

    private void initGraph(final Section section, final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        final Station upStation = section.getUpStation();
        final Station downStation = section.getDownStation();
        graph.addVertex(upStation);
        graph.addVertex(downStation);
        graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
    }
}
