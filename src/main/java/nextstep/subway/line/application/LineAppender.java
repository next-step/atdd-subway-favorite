package nextstep.subway.line.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LineAppender {
  private final LineRepository lineRepository;

  public Line append(Line line) {
    return lineRepository.save(line);
  }
}
