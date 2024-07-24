package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineSection;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayGraph {
  private static final String STATION_NOT_FOUND = "추가하는 구간의 상/하행역이 존재하지 않습니다.";
  private static final double EPSILON = 10e-7;

  private final WeightedMultigraph<Station, LineSectionEdge> graph;

  public SubwayGraph(WeightedMultigraph<Station, LineSectionEdge> graph) {
    this.graph = graph;
  }

  public SubwayGraph() {
    this(new WeightedMultigraph<>(LineSectionEdge.class));
  }

  public void addStation(Station station) {
    graph.addVertex(station);
  }

  public void addLineSection(LineSection lineSection) {
    Station upStation = lineSection.getUpStation();
    Station downStation = lineSection.getDownStation();
    validate(upStation, downStation);
    LineSectionEdge edge = LineSectionEdge.of(lineSection);
    graph.addEdge(upStation, downStation, edge);
    graph.setEdgeWeight(edge, lineSection.getDistance());
  }

  private void validate(Station upStation, Station downStation) {
    if (!graph.containsVertex(upStation) || !graph.containsVertex(downStation)) {
      throw new IllegalArgumentException(STATION_NOT_FOUND);
    }
  }

  public void addLine(Line line) {
    line.getStations().forEach(this::addStation);
    line.getLineSections().getSections().forEach(this::addLineSection);
  }

  public Path getShortestPath(Station source, Station sink) {
    validate(source, sink);
    DijkstraShortestPath<Station, LineSectionEdge> shortestPath = new DijkstraShortestPath<>(graph);
    GraphPath<Station, LineSectionEdge> path = shortestPath.getPath(source, sink);
    if (path == null) {
      return Path.empty();
    }
    return Path.of(path.getVertexList(), (long) path.getWeight());
  }

  public boolean isSame(SubwayGraph that) {
    if (!graph.vertexSet().equals(that.graph.vertexSet())) {
      return false;
    }
    if (graph.edgeSet().size() != that.graph.edgeSet().size()) {
      return false;
    }
    for (LineSectionEdge edge : graph.edgeSet()) {
      Station source = graph.getEdgeSource(edge);
      Station target = graph.getEdgeTarget(edge);
      LineSectionEdge thatEdge = that.graph.getEdge(source, target);
      if (thatEdge == null) {
        return false;
      }
      if (Math.abs(graph.getEdgeWeight(edge) - that.graph.getEdgeWeight(thatEdge)) > EPSILON) {
        return false;
      }
    }
    return true;
  }
}
