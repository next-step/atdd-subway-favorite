package nextstep.subway.path.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.SubwayGraph;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GraphService {
  private final LineService lineService;

  public SubwayGraph loadGraph() {
    SubwayGraph graph = new SubwayGraph();
    List<Line> lines = lineService.findAllLines();
    lines.forEach(graph::addLine);
    return graph;
  }
}
