package nextstep.path.domain;

import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.path.exception.PathNotFoundException;
import nextstep.station.domain.Station;
import org.jgrapht.GraphPath;
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

    public Path findShortestPath(final Station sourceStation, final Station targetStation) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = buildGraph();

        if (!(graph.containsVertex(sourceStation) && graph.containsVertex(targetStation))) {
            throw new PathNotFoundException();
        }

        final DijkstraShortestPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph);

        final GraphPath<Station, DefaultWeightedEdge> shortestPath =
                Optional.ofNullable(path.getPath(sourceStation, targetStation))
                        .orElseThrow(PathNotFoundException::new);

        return new Path(shortestPath.getVertexList(), (int) shortestPath.getWeight());
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
