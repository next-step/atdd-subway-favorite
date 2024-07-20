package nextstep.subway.line.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.exception.LineNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LineReader {
  private final LineRepository lineRepository;

  public List<Line> read() {
    return lineRepository.findAll();
  }

  public Line readById(Long id) {
    return lineRepository.findById(id).orElseThrow(() -> new LineNotFoundException(id));
  }
}
