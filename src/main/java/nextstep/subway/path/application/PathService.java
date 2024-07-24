package nextstep.subway.path.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.path.application.dto.PathRequest;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.station.application.StationReader;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PathService {
  private final GraphService graphService;
  private final StationReader stationReader;

  public Path findPath(PathRequest request) {
    Station source = stationReader.readById(request.getSource());
    Station sink = stationReader.readById(request.getTarget());
    SubwayGraph graph = graphService.loadGraph();
    return graph.getShortestPath(source, sink);
  }
}
