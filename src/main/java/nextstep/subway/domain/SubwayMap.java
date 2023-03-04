package nextstep.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SubwayMap {
    private List<Line> lines;

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
    }

    public Path findPath(Station source, Station target) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        addVertex(graph);
        addEdge(graph);

        GraphPath<Station, SectionEdge> result = findShortedPath(source, target, graph);

        List<Section> sections = result.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());

        return new Path(new Sections(sections));
    }

    private static GraphPath<Station, SectionEdge> findShortedPath(Station source, Station target, SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(source, target);
    }

    private void addEdge(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역의 연결 정보(간선)을 등록 (역방향 포함)
        List<Section> sections =
                Stream.concat(
                        flatMap(lines, line -> line.getSections().stream()).stream(),
                        flatMap(lines, line -> line.getOppositeSections().stream()).stream()
                ).collect(Collectors.toList());

        sections.forEach(section -> addEdge(graph, section));
    }

    private static void addEdge(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, Section it) {
        SectionEdge sectionEdge = SectionEdge.of(it);
        graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
        graph.setEdgeWeight(sectionEdge, it.getDistance());
    }

    private void addVertex(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역(정점)을 등록
        List<Station> stations = flatMap(lines, line -> line.getStations().stream());
        stations.forEach(graph::addVertex);
    }

    private <T, R> List<R> flatMap(List<T> list, Function<T, Stream<R>> function) {
        return list.stream()
                .flatMap(function)
                .collect(Collectors.toList());
    }
}
