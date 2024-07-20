package nextstep.subway.line.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.application.dto.UpdateLineRequest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LineModifier {
  private final LineReader lineReader;
  private final LineRepository lineRepository;

  public Line modify(Long id, UpdateLineRequest request) {
    Line line = lineReader.readById(id);
    line.changeName(request.getName());
    line.changeColor(request.getColor());
    return lineRepository.save(line);
  }
}
